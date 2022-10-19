<template>
  <ContentField>
    <table class="table table-hover">
      <thead>
        <tr>
          <th></th>
          <th>player</th>
          <th></th>
          <th>rating</th>
          <th></th>
        </tr>
      </thead>
      <tbody sy>
        <tr v-for="user in users" :key="user.id" style="vertical-align: middle;">
          <td style="width: 15%;"></td>
          <td style="width: 5%;">
            <img :src="'/static/images/avatar/' + user.photo + '.png'" alt="" class="user-photo">
          </td>
          <td>
            <span class="user-username">{{ user.username }}</span>
          </td>
          <td>
            {{ user.rating }}
          </td>
          <td style="width: 15%;"></td>
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
            <a class="page-link" href="javascript:void(0)" aria-label="Previous">
              <span aria-hidden="true">&nbsp;&laquo;&nbsp;</span>
            </a>
          </li>
          <li @click="to_page(page.number)" :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" >
            <a class="page-link" href="javascript:void(0)">{{ page.number }}</a>
          </li>
          <li class="page-item" @click="to_page(-1)">
            <a class="page-link" href="javascript:void(0)" aria-label="Previous">
              <span aria-hidden="true">&nbsp;&raquo;&nbsp;</span>
            </a>
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
          url: "http://127.0.0.1:3000/api/ranklist/getlist/",
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
          }
        })
      };

      pull_pages(current_page);
      
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
  border-radius: 50%;
  width: 7vh;
  height: 7vh;
  object-fit: cover;
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