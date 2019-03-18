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
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|JsonIgnoreProperties
import|;
end_import

begin_comment
comment|/**  * Describes a Wordpress Category  */
end_comment

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|Category
specifier|public
class|class
name|Category
extends|extends
name|Classifier
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|8542893392638055010L
decl_stmt|;
DECL|field|parent
specifier|private
name|Integer
name|parent
decl_stmt|;
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
block|}
end_class

end_unit

