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

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|DeletedModel
specifier|public
class|class
name|DeletedModel
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|deleted
specifier|private
name|Boolean
name|deleted
decl_stmt|;
DECL|field|previous
specifier|private
name|T
name|previous
decl_stmt|;
DECL|method|DeletedModel ()
specifier|public
name|DeletedModel
parameter_list|()
block|{      }
DECL|method|getDeleted ()
specifier|public
name|Boolean
name|getDeleted
parameter_list|()
block|{
return|return
name|deleted
return|;
block|}
DECL|method|setDeleted (Boolean deleted)
specifier|public
name|void
name|setDeleted
parameter_list|(
name|Boolean
name|deleted
parameter_list|)
block|{
name|this
operator|.
name|deleted
operator|=
name|deleted
expr_stmt|;
block|}
DECL|method|getPrevious ()
specifier|public
name|T
name|getPrevious
parameter_list|()
block|{
return|return
name|previous
return|;
block|}
DECL|method|setPrevious (T previous)
specifier|public
name|void
name|setPrevious
parameter_list|(
name|T
name|previous
parameter_list|)
block|{
name|this
operator|.
name|previous
operator|=
name|previous
expr_stmt|;
block|}
block|}
end_class

end_unit

