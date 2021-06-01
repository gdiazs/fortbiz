<!--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->
<script>
import {mapGetters} from 'vuex';
 export default {
   name: "b-footer",
   computed:{
     ...mapGetters(['getEnv'])
   }
 }
 </script>
 <template>
  <footer class="page-footer font-small ">
    <div class="text-right pr-3 py-2 bg-secondary text-white">
      >_ aedeatech © 2020 Derechos reservados <span v-if="(getEnv !== 'prod')">| {{getEnv}}</span>
    </div>
  </footer>
 </template>
 <style scoped lang="scss">
   .page-footer {
     width: 100%;
     position: fixed;
     bottom: 0;
   }
 </style>
