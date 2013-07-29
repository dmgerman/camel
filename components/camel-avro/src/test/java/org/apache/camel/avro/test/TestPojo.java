begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|TestPojo
specifier|public
class|class
name|TestPojo
block|{
DECL|field|pojoName
specifier|private
name|String
name|pojoName
decl_stmt|;
DECL|method|getPojoName ()
specifier|public
name|String
name|getPojoName
parameter_list|()
block|{
return|return
name|pojoName
return|;
block|}
DECL|method|setPojoName (String pojoName)
specifier|public
name|void
name|setPojoName
parameter_list|(
name|String
name|pojoName
parameter_list|)
block|{
name|this
operator|.
name|pojoName
operator|=
name|pojoName
expr_stmt|;
block|}
block|}
end_class

end_unit

