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
export default {
    name: 'InputSearch',
    props: ['state', 'label', 'options', 'cache', 'placeholder'],
    data(){
      return {
        words: null,
        selectedIdex: -1,
        isVisible: false,
        isScrolling: false,
      }
    },
    methods: {
      onItemSelected(selected){
        this.$emit('optionClicked', selected);
        this.words = selected.description;
        this.isScrolling = false;
        this.isVisible = false;


      },
      onKeyGoUp(){
        if(this.selectedIdex !== -1){
          this.selectedIdex--;
          this.isVisible = true;
        }
      },
      onKeyGoDown(){
        if(this.selectedIdex >= -1 && this.selectedIdex < this.options.length){
          this.selectedIdex++;
          this.isVisible = true;
        }
      },
      onUserPressEnter(){
        this.onItemSelected(this.options[this.selectedIdex]);
        this.selectedIdex = -1;        
      },
      onLeave(){
        this.selectedIdex = -1;
        setTimeout(() => {
          if(!this.isScrolling){
            this.isVisible = false;
          }
          
        }, 150);
      },
      onFocus(){
        this.isVisible = true;
        this.isScrolling = false;
      }
    },
    watch: {
      words: function(value){
        this.$emit('typing', value);
      }
    }
}
</script>

<template>
    <div class="position-relative" > 
        <b-form-group :label="label" >
          <b-form-input 
            v-on:keyup.enter="onUserPressEnter" 
            v-bind="$props" v-model="words" 
            debounce="250" autocomplete="off" 
            v-on:keyup.up="onKeyGoUp" 
            v-on:keyup.down="onKeyGoDown"
            v-on:keyup.esc="isVisible = false"
            v-on:blur.native="onLeave"
            v-on:focus.native="onFocus"
          />
          <slot />
          <div  v-if="isVisible" v-on:keyup.esc="isVisible = false"  class="shadow pointer position-absolute w-100 z-index-2  result-height" @scroll="isScrolling = true">
            <b-list-group>
              <div @click="onItemSelected(item)" class="hover bg-white" :class="{'selected': index === selectedIdex}" v-for="(item, index) in options" :key="item.value">
                <slot name="item"  v-bind:data="item" >
                  <b-list-group-item>
                    {{item.label}}
                  </b-list-group-item>
                </slot>
              </div>
            </b-list-group>
          </div>
        </b-form-group >

      
    </div>
</template>
<style lang="scss" scoped>
  .z-index-2{
    z-index: 2;
  }
  .result-height{
    max-height: 25em;
    overflow-y: scroll ;
  }
</style>