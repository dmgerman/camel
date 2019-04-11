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
name|Relationship
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
literal|"book"
argument_list|)
DECL|class|MyBook
specifier|public
class|class
name|MyBook
block|{
annotation|@
name|Id
DECL|field|isbn
specifier|private
name|String
name|isbn
decl_stmt|;
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
annotation|@
name|Relationship
argument_list|(
literal|"author"
argument_list|)
DECL|field|author
specifier|private
name|MyAuthor
name|author
decl_stmt|;
DECL|method|getIsbn ()
specifier|public
name|String
name|getIsbn
parameter_list|()
block|{
return|return
name|isbn
return|;
block|}
DECL|method|setIsbn (String isbn)
specifier|public
name|void
name|setIsbn
parameter_list|(
name|String
name|isbn
parameter_list|)
block|{
name|this
operator|.
name|isbn
operator|=
name|isbn
expr_stmt|;
block|}
DECL|method|getTitle ()
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
DECL|method|setTitle (String title)
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
DECL|method|getAuthor ()
specifier|public
name|MyAuthor
name|getAuthor
parameter_list|()
block|{
return|return
name|author
return|;
block|}
DECL|method|setAuthor (MyAuthor author)
specifier|public
name|void
name|setAuthor
parameter_list|(
name|MyAuthor
name|author
parameter_list|)
block|{
name|this
operator|.
name|author
operator|=
name|author
expr_stmt|;
block|}
block|}
end_class

end_unit

