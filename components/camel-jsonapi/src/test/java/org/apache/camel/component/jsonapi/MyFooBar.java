begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jsonapi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jsonapi
package|;
end_package

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|jasminb
operator|.
name|jsonapi
operator|.
name|annotations
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|jasminb
operator|.
name|jsonapi
operator|.
name|annotations
operator|.
name|Type
import|;
end_import

begin_comment
comment|/** * * JSON API test object * */
end_comment

begin_class
annotation|@
name|Type
argument_list|(
literal|"foobar"
argument_list|)
DECL|class|MyFooBar
specifier|public
class|class
name|MyFooBar
block|{
annotation|@
name|Id
DECL|field|foo
specifier|private
name|String
name|foo
decl_stmt|;
DECL|method|MyFooBar (String foo)
specifier|public
name|MyFooBar
parameter_list|(
name|String
name|foo
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
block|}
DECL|method|getFoo ()
specifier|public
name|String
name|getFoo
parameter_list|()
block|{
return|return
name|foo
return|;
block|}
DECL|method|setFoo (String foo)
specifier|public
name|void
name|setFoo
parameter_list|(
name|String
name|foo
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
block|}
block|}
end_class

end_unit

