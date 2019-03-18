begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|swagger
package|;
end_package

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|ApiModel
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|ApiModelProperty
import|;
end_import

begin_class
annotation|@
name|ApiModel
argument_list|(
name|description
operator|=
literal|"Order line"
argument_list|)
DECL|class|LineItem
specifier|public
class|class
name|LineItem
block|{
DECL|field|isbn
specifier|private
name|String
name|isbn
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|ApiModelProperty
argument_list|(
name|value
operator|=
literal|"ISBN of the book"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
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
annotation|@
name|ApiModelProperty
argument_list|(
name|value
operator|=
literal|"Name of the book"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

