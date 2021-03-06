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
name|java
operator|.
name|util
operator|.
name|List
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
literal|"Represents a book order"
argument_list|)
DECL|class|BookOrder
specifier|public
class|class
name|BookOrder
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|items
specifier|private
name|List
argument_list|<
name|LineItem
argument_list|>
name|items
decl_stmt|;
annotation|@
name|ApiModelProperty
argument_list|(
name|value
operator|=
literal|"The id of the order"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|ApiModelProperty
argument_list|(
name|value
operator|=
literal|"The books ordered"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|method|getItems ()
specifier|public
name|List
argument_list|<
name|LineItem
argument_list|>
name|getItems
parameter_list|()
block|{
return|return
name|items
return|;
block|}
DECL|method|setItems (List<LineItem> items)
specifier|public
name|void
name|setItems
parameter_list|(
name|List
argument_list|<
name|LineItem
argument_list|>
name|items
parameter_list|)
block|{
name|this
operator|.
name|items
operator|=
name|items
expr_stmt|;
block|}
block|}
end_class

end_unit

