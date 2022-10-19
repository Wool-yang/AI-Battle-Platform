<template>
  <div class="container"> 
    <div class="row">
      <div class="col-3">
        <div class="card mt-4">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="" class="user-photo"/>
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card mt-4">
          <div class="card-header ch-style">
            <div class="ch-title">My Bots</div>
            <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-modal">create</button>
            
            <!-- Create-Modal -->
            <div class="modal fade" id="add-bot-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
              <div class="modal-dialog modal-xl">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Create a bot</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <div class="mb-3">
                      <label for="add-bot-title" class="form-label">bot title</label>
                      <input v-model="bot_add.title" type="text" class="form-control" id="add-bot-title" placeholder="please input the title of bot">
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-description" class="form-label">bot description</label>
                      <textarea v-model="bot_add.description" class="form-control" id="add-bot-description" placeholder="please input the description of bot" rows="3"></textarea>
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-code" class="form-label">bot code</label>
                      <div class="select-style">
                        <div style="width: 25%">
                          <div>
                            language
                          </div>
                          <select v-model="aceEdit.language" class="form-select" aria-label="Default select example">
                            <option selected value="java">Java</option>
                            <option value="c_cpp">C++</option>
                          </select>
                        </div>
                        

                        <div style="width: 25%">
                          <div>
                            fontSize
                          </div>
                          <input v-model="fontSize" @change="change_fontSize(fontSize)" type="range" class="form-range" min="10" max="50" step="1" id="fontSizeRange">
                        </div>
                        

                        <div style="width: 30%">
                          <div>
                            theme
                          </div>
                          <select v-model="aceEdit.theme" class="form-select" aria-label="Default select example">
                            <option selected value="textmate">TextMate</option>
                            <option value="tomorrow_night_eighties">Tomorrow Night 80s</option>
                          </select>
                        </div>
                      </div>
                      
                      <VAceEditor
                        v-model:value="bot_add.content"
                        @init="editorInit"
                        :lang="aceEdit.language"
                        :options="aceEdit.options"
                        :theme="aceEdit.theme"
                        :style="aceStyle"/>

                    </div>
                  </div>
                  <div class="modal-footer">
                    <div class="error-message">{{ bot_add.error_message }}</div>
                    <button type="button" @click="add_bot" class="btn btn-primary">create</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">close</button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="card-body">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th style="width: 30%">name</th>
                  <th style="width: 45%">create time</th>
                  <th></th>
                </tr>
              </thead>
              <tbody sy>
                <tr v-for="bot in bots" :key="bot.id" style="vertical-align: middle;">
                  <td>{{ bot.title }}</td>
                  <td>{{ bot.createtime }}</td>
                  <td>
                    <button type="button" @click="set_bot_now(bot)" class="btn btn-secondary" style="margin-right: 7px" data-bs-toggle="modal" data-bs-target="#update-bot-modal">update</button>
                    <button type="button" @click="set_bot_now(bot)" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#delete-bot-modal">delete</button>
                  </td>
                </tr>
              </tbody>
            </table>

            <!-- Update-Modal -->
            <div class="modal fade" id="update-bot-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
              <div class="modal-dialog modal-xl">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Update the bot</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <div class="mb-3">
                      <label for="add-bot-title" class="form-label">bot title</label>
                      <input v-model="bot_now.title" type="text" class="form-control" id="add-bot-title" placeholder="please input the title of bot">
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-description" class="form-label">bot description</label>
                      <textarea v-model="bot_now.description" class="form-control" id="add-bot-description" placeholder="please input the description of bot" rows="3"></textarea>
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-code" class="form-label">bot code</label>
                      <div class="select-style">
                        <div style="width: 25%">
                          <div>
                            language
                          </div>
                          <select v-model="aceEdit.language" class="form-select" aria-label="Default select example">
                            <option selected value="java">Java</option>
                            <option value="c_cpp">C++ (not available now)</option>
                          </select>
                        </div>
                        

                        <div style="width: 25%">
                          <div>
                            fontSize
                          </div>
                          <input v-model="fontSize" @change="change_fontSize(fontSize)" type="range" class="form-range" min="10" max="50" step="1" id="fontSizeRange">
                        </div>
                        

                        <div style="width: 30%">
                          <div>
                            theme
                          </div>
                          <select v-model="aceEdit.theme" class="form-select" aria-label="Default select example">
                            <option selected value="textmate">TextMate</option>
                            <option value="tomorrow_night_eighties">Tomorrow Night 80s</option>
                          </select>
                        </div>
                      </div>
                      
                      <VAceEditor
                        v-model:value="bot_now.content"
                        @init="editorInit"
                        :lang="aceEdit.language"
                        :options="aceEdit.options"
                        :theme="aceEdit.theme"
                        :style="aceStyle"/>

                    </div>
                  </div>
                  <div class="modal-footer">
                    <div class="error-message">{{ bot_now.error_message }}</div>
                    <button type="button" @click="update_bot(bot_now)" class="btn btn-primary">update</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">close</button>
                  </div>
                </div>
              </div>
            </div>

            <!-- delete-Modal -->
            <div class="modal fade" id="delete-bot-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
              <div class="modal-dialog modal-lg">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Delete confirmation</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <div>You confirm that you want to delete this bot, this action cannot be undone</div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" @click="remove_bot(bot_now)" class="btn btn-danger">delete</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">close</button>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div> 
</template>

<script>
  import { ref, reactive } from 'vue';
  import $ from 'jquery';
  import { useStore } from 'vuex';
  import { Modal } from "bootstrap/dist/js/bootstrap";

  import { VAceEditor } from 'vue3-ace-editor';
  import ace from 'ace-builds';

  export default {
    name: "UserBotIndex",
    components: {
      VAceEditor
    },
    setup() {
      ace.config.set(
              "basePath", 
              "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/");
      
      const store = useStore();
      let bots = ref([]);

      const aceEdit = reactive({
        language: 'java',
        theme: 'textmate',
        options: {
          enableBasicAutocompletion: true, 
          enableSnippets: true, 
          enableLiveAutocompletion: true, 
          enableMultiselect: true, 
          tabSize: 4, 
          howPrintMargin: false, 
          highlightActiveLine: true, 
          fontFamily: 'Consolas'
        }
      });

      const aceStyle = reactive({
        height: '300px',
        'font-size': '20px'
      });
      let fontSize = ref(20);
      const change_fontSize = (num) => {
        aceStyle['font-size'] = num +'px';
      }

      const bot_add = reactive({
        title: "",
        description: "",
        content: "",
        error_message: "",
      });

      const bot_now = reactive({
        id: "",
        title: "",
        description: "",
        content: "",
        error_message: "",
      });

      const set_bot_now = (bot) => {
        bot_now.id = bot.id;
        bot_now.title = bot.title;
        bot_now.description = bot.description;
        bot_now.content = bot.content;
      }

      const refresh_bots = () => {
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/bot/getall/",
          type: "GET",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success(resp) {
            bots.value = resp;
          }
        })
      };

      refresh_bots();

      const add_bot = () => {
        bot_add.error_message = "";
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/bot/add/",
          type: "POST",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          data: {
            title: bot_add.title,
            description: bot_add.description,
            content: bot_add.content
          },
          success(resp) {
            if (resp.error_message === "success") {
              refresh_bots();
              reset_add();
              Modal.getInstance("#add-bot-modal").hide();
            }
            else {
              bot_add.error_message = resp.error_message;
            }
          }
        })
      }

      const remove_bot = (bot) => {
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/bot/remove/",
          type: "DELETE",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          data: {
            bot_id: bot.id
          },
          success(resp) {
            if (resp.error_message === "success") {
              refresh_bots();
              Modal.getInstance("#delete-bot-modal").hide();
            }
          }
        });
      }

      const update_bot = (bot) => {
        bot.error_message = "";
        $.ajax({
          url: "http://127.0.0.1:3000/api/user/bot/update/",
          type: "PUT",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          data: {
            title: bot.title,
            description: bot.description,
            content: bot.content,
            bot_id: bot.id
          },
          success(resp) {
            if (resp.error_message === "success") {
              refresh_bots();
              Modal.getInstance('#update-bot-modal').hide();
            }
            else {
              bot.error_message = resp.error_message;
            }
          }
        })
      }

      const reset_add = () => {
        bot_add.title = "";
        bot_add.description = "";
        bot_add.content = "";
      }

      return {
        bots,
        bot_add,
        add_bot,
        remove_bot,
        update_bot,
        aceEdit,
        aceStyle,
        change_fontSize,
        fontSize,
        bot_now,
        set_bot_now
      }
    }
  };
</script>

<style scoped>
  .ch-style {
    display:flex; 
    flex-direction:row; 
    align-items: center; 
    justify-content: space-between;
  }
  .ch-title {
    font-size: 150%;
  }
  .error-message {
    color: red;
  }
  .select-style {
    display:flex; 
    flex-direction:row; 
    align-items: center; 
    justify-content: space-between;
    margin: 0 3% 10px 3%;
  }
  .user-photo {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
</style>