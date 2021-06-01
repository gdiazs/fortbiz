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
    name: "StepsSection",
    props: {
        currentStep: { default: 0},
        stepsCount: {default: 0}
    }
}
</script>
<template>
    <div class="display-absolute mb-4">
        <div class="d-flex justify-content-center">
            <div class="bar-step m-1 p-1 rounded border border-primary" :class="{'bg-primary text-white': currentStep === step, 'bg-white': currentStep !== step}" v-for="step in stepsCount" :key="step">
                Paso {{step}}
            </div>
        </div>
         <div class="bar border-1 border-bottom border-primary mt-custom w-100"></div>
    </div>
</template>

<style>
    .mt-custom{
        margin-top: -1.2em;
    }
    .bar{
        z-index: 1;
    }
    .bar-step{
        z-index: 100;
    }

</style>