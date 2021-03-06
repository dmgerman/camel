begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * JSON API test object  */
end_comment

begin_class
annotation|@
name|Type
argument_list|(
literal|"author"
argument_list|)
DECL|class|MyAuthor
specifier|public
class|class
name|MyAuthor
block|{
annotation|@
name|Id
DECL|field|authorId
specifier|private
name|String
name|authorId
decl_stmt|;
DECL|field|firstName
specifier|private
name|String
name|firstName
decl_stmt|;
DECL|field|lastName
specifier|private
name|String
name|lastName
decl_stmt|;
DECL|method|getAuthorId ()
specifier|public
name|String
name|getAuthorId
parameter_list|()
block|{
return|return
name|authorId
return|;
block|}
DECL|method|setAuthorId (String authorId)
specifier|public
name|void
name|setAuthorId
parameter_list|(
name|String
name|authorId
parameter_list|)
block|{
name|this
operator|.
name|authorId
operator|=
name|authorId
expr_stmt|;
block|}
DECL|method|getFirstName ()
specifier|public
name|String
name|getFirstName
parameter_list|()
block|{
return|return
name|firstName
return|;
block|}
DECL|method|setFirstName (String firstName)
specifier|public
name|void
name|setFirstName
parameter_list|(
name|String
name|firstName
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
block|}
DECL|method|getLastName ()
specifier|public
name|String
name|getLastName
parameter_list|()
block|{
return|return
name|lastName
return|;
block|}
DECL|method|setLastName (String lastName)
specifier|public
name|void
name|setLastName
parameter_list|(
name|String
name|lastName
parameter_list|)
block|{
name|this
operator|.
name|lastName
operator|=
name|lastName
expr_stmt|;
block|}
block|}
end_class

end_unit

