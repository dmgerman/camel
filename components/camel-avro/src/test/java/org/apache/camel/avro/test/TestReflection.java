begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.avro.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|test
package|;
end_package

begin_interface
DECL|interface|TestReflection
specifier|public
interface|interface
name|TestReflection
block|{
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
DECL|method|setName (String name)
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|getAge ()
name|int
name|getAge
parameter_list|()
function_decl|;
DECL|method|setAge (int age)
name|void
name|setAge
parameter_list|(
name|int
name|age
parameter_list|)
function_decl|;
DECL|method|increaseAge (int age)
name|int
name|increaseAge
parameter_list|(
name|int
name|age
parameter_list|)
function_decl|;
DECL|method|setTestPojo (TestPojo testPojo)
name|void
name|setTestPojo
parameter_list|(
name|TestPojo
name|testPojo
parameter_list|)
function_decl|;
DECL|method|getTestPojo ()
name|TestPojo
name|getTestPojo
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

