begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
package|;
end_package

begin_class
DECL|class|Foo
specifier|public
class|class
name|Foo
block|{
DECL|field|first
specifier|private
name|String
name|first
decl_stmt|;
DECL|field|last
specifier|private
name|String
name|last
decl_stmt|;
DECL|method|getFirst ()
specifier|public
name|String
name|getFirst
parameter_list|()
block|{
return|return
name|first
return|;
block|}
DECL|method|setFirst (String first)
specifier|public
name|void
name|setFirst
parameter_list|(
name|String
name|first
parameter_list|)
block|{
name|this
operator|.
name|first
operator|=
name|first
expr_stmt|;
block|}
DECL|method|getLast ()
specifier|public
name|String
name|getLast
parameter_list|()
block|{
return|return
name|last
return|;
block|}
DECL|method|setLast (String last)
specifier|public
name|void
name|setLast
parameter_list|(
name|String
name|last
parameter_list|)
block|{
name|this
operator|.
name|last
operator|=
name|last
expr_stmt|;
block|}
block|}
end_class

end_unit

