<template>
  <ContentField>
    <table class="table table-hover" style="text-align: center">
      <thead>
        <tr>
          <th>playerA</th>
          <th>playerB</th>
          <th>winner</th>
          <th>time</th>
          <th></th>
        </tr>
      </thead>
      <tbody sy>
        <tr v-for="record in records" :key="record.record.id" style="vertical-align: middle;">
          <td>
            <img :src="record.a_photo" alt="" class="record-user-photo">
            &nbsp;
            <span class="record-user-username">{{ record.a_username }}</span>
          </td>
          <td>
            <img :src="record.b_photo" alt="" class="record-user-photo">
            &nbsp;
            <span class="record-user-username">{{ record.b_username }}</span>
          </td>
          <td>
            {{ record.result }}
          </td>
          <td>
            {{ record.record.createtime }}
          </td>
          <td>
            <button type="button" @click="open_record_content(record.record.id)" class="btn btn-primary">watch</button>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="mypagination">
      <div style="margin-right: 1%;">
        Total {{ max_pages }} pages
      </div>
      <input v-model="jump_page" placeholder="jump" type="Number" class="form-control" style="width: 15vh; margin-right: 1%; ">
      <button type="button" @click="jump_to()" class="btn btn-primary" style="margin-right: 1%;">Jump</button>
      <nav style="margin-top: 2.5vh;" aria-label="Page navigation example">
        <ul class="pagination">
          <li class="page-item" @click="to_page(-2)">
            <a class="page-link" href="javascript:void(0)">Previous</a>
          </li>
          <li @click="to_page(page.number)" :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" >
            <a class="page-link" href="javascript:void(0)">{{ page.number }}</a>
          </li>
          <li class="page-item" @click="to_page(-1)">
            <a class="page-link" href="javascript:void(0)">Next</a>
          </li>
        </ul>
      </nav>
    </div>
  </ContentField>
</template>

<script>
  import ContentField from '@/components/ContentField.vue';
  import { useStore } from 'vuex';
  import $ from 'jquery'
  import { ref } from 'vue';
  import router from '@/router/index';

  export default {
    name: "RecordIndex",
    components: { ContentField },
    setup() {
      const store = useStore();
      let current_page = 1;
      let pages = ref([]);
      let total_records = 0;
      let records = ref([]);
      let max_pages = ref(0);
      let jump_page = ref(1);
      
      const to_page = page_number => {
        if (page_number === -2) {
          page_number = current_page - 1;
        } else if (page_number === -1) {
          page_number = current_page + 1;
        }
        
        max_pages.value = parseInt(Math.ceil(total_records / 10));

        if (page_number >= 1 && page_number <= max_pages.value) {
          pull_pages(page_number);
        }
      }

      const jump_to = () => {
        if (jump_page.value <= max_pages.value && jump_page.value >= 1) {
          pull_pages(parseInt(jump_page.value));
        }
      }

      const update_pages = () => {
        max_pages.value = parseInt(Math.ceil(total_records / 10));
        let new_pages = [];
        for (let i = current_page - 2; i <= current_page + 2; i ++ ) {
          if (i >= 1 && i <= max_pages.value) {
            new_pages.push({
              number: i,
              is_active: i === current_page ? "active" : ""
            });
          }
        }
        pages.value = new_pages;
      }

      const pull_pages = page => {
        current_page = page;
        $.ajax({
          url: "http://localhost:3000/record/getlist/",
          type: "GET",
          data: {
            page
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success(resp) {
            records.value = resp.records;
            total_records = resp.records_count;
            max_pages.value = parseInt(Math.ceil(total_records / 10));
            update_pages();
          },
          error(resp) {
            console.log(resp);
          }
        })
      };

      pull_pages(current_page);
      
      // 将传递过来的 String map 转换成数组
      const stringTo2DMap = map => {
        let g = [];
        for (let i = 0, k = 0; i < 17; i ++ ) {
          let line = [];
          for (let j = 0; j < 18; j ++, k ++ ) {
            if (map[k] === '0') line.push(0);
            else line.push(1);
          }
          g.push(line);
        }
        return g;
      }

      const open_record_content = recordId => {
          for (const record of records.value) {
            if (record.record.id === recordId) {
              store.commit("updateIsRecord", true);

              store.commit("updateRecordUsernames", {
                a_username: record.a_username,
                b_username: record.b_username
              });
              
              store.commit("updateGame", {
                map: stringTo2DMap(record.record.map),
                a_id: record.record.aid,
                a_sx: record.record.asx,
                a_sy: record.record.asy,
                b_id: record.record.bid,
                b_sx: record.record.bsx,
                b_sy: record.record.bsy
              });
              
              store.commit("updateSteps", {
                a_steps: record.record.asteps,
                b_steps: record.record.bsteps,
              })

              store.commit("updateRecordWinner", record.record.winner);

              router.push({
                name: "record_content",
                params: {
                  recordId
                }
              })
              break;
            }
          } 
      }

      return {
        records,
        total_records,
        open_record_content,
        pages,
        to_page,
        max_pages,
        jump_page,
        jump_to
      }
    }
};
</script>

<style scoped>
img.record-user-photo {
  width: 5vh;
  border-radius: 50%;
}
div.mypagination {
  margin-right: 5%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-end;
  flex-wrap: wrap;
}
</style>