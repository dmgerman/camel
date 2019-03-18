begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|model
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|Page
specifier|public
class|class
name|Page
extends|extends
name|TextPublishable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3517585398919756299L
decl_stmt|;
DECL|field|parent
specifier|private
name|Integer
name|parent
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"menu_order"
argument_list|)
DECL|field|menuOrder
specifier|private
name|Integer
name|menuOrder
decl_stmt|;
DECL|method|Page ()
specifier|public
name|Page
parameter_list|()
block|{      }
DECL|method|getParent ()
specifier|public
name|Integer
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
DECL|method|setParent (Integer parent)
specifier|public
name|void
name|setParent
parameter_list|(
name|Integer
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
DECL|method|getMenuOrder ()
specifier|public
name|Integer
name|getMenuOrder
parameter_list|()
block|{
return|return
name|menuOrder
return|;
block|}
DECL|method|setMenuOrder (Integer menuOrder)
specifier|public
name|void
name|setMenuOrder
parameter_list|(
name|Integer
name|menuOrder
parameter_list|)
block|{
name|this
operator|.
name|menuOrder
operator|=
name|menuOrder
expr_stmt|;
block|}
block|}
end_class

end_unit

