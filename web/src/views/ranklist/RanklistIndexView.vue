<template>
  <ContentField>
    <table class="table table-hover" style="text-align: center">
      <thead>
        <tr>
          <th>player</th>
          <th>rating</th>
        </tr>
      </thead>
      <tbody sy>
        <tr v-for="user in users" :key="user.id" style="vertical-align: middle;">
          <td>
            <img :src="user.photo" alt="" class="user-photo">
            &nbsp;
            <span class="user-username">{{ user.username }}</span>
          </td>
          <td>
            {{ user.rating }}
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

  export default {
    name: "RanklistIndex",
    components: { ContentField },
    setup() {
      const store = useStore();
      let current_page = 1;
      let pages = ref([]);
      let total_users = 0;
      let users = ref([]);
      let max_pages = ref(0);
      let jump_page = ref(1);
      
      const to_page = page_number => {
        if (page_number === -2) {
          page_number = current_page - 1;
        } else if (page_number === -1) {
          page_number = current_page + 1;
        }
        
        max_pages.value = parseInt(Math.ceil(total_users / 10));

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
        max_pages.value = parseInt(Math.ceil(total_users / 10));
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
          url: "http://localhost:3000/ranklist/getlist/",
          type: "GET",
          data: {
            page
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success(resp) {
            users.value = resp.users;
            total_users = resp.users_count;
            max_pages.value = parseInt(Math.ceil(total_users / 10));
            update_pages();
          },
          error(resp) {
            console.log(resp);
          }
        })
      };

      pull_pages(current_page);
      
      setInterval(() => {
        console.log("current_page: " + current_page + "\n" +
                    "total_users: " + total_users + "\n" + 
                    "jump_page: " + jump_page.value + "\n" +
                    "max_pages: " + max_pages.value + "\n");
      }, 500);
      return {
        users,
        total_users,
        to_page,
        max_pages,
        jump_page,
        jump_to,
        pages
      }
    }
};
</script>

<style scoped>
img.user-photo {
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