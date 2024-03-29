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

begin_class
DECL|class|CategorySearchCriteria
specifier|public
class|class
name|CategorySearchCriteria
extends|extends
name|ClassifierSearchCriteria
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3124924225447605233L
decl_stmt|;
DECL|field|orderBy
specifier|private
name|CategoryOrderBy
name|orderBy
decl_stmt|;
DECL|field|parent
specifier|private
name|Integer
name|parent
decl_stmt|;
DECL|method|CategorySearchCriteria ()
specifier|public
name|CategorySearchCriteria
parameter_list|()
block|{      }
DECL|method|getOrderBy ()
specifier|public
name|CategoryOrderBy
name|getOrderBy
parameter_list|()
block|{
return|return
name|orderBy
return|;
block|}
DECL|method|setOrderBy (CategoryOrderBy orderBy)
specifier|public
name|void
name|setOrderBy
parameter_list|(
name|CategoryOrderBy
name|orderBy
parameter_list|)
block|{
name|this
operator|.
name|orderBy
operator|=
name|orderBy
expr_stmt|;
block|}
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

