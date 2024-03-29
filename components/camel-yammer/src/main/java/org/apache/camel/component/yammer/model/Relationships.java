begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
operator|.
name|model
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
DECL|class|Relationships
specifier|public
class|class
name|Relationships
block|{
DECL|field|subordinates
specifier|private
name|List
argument_list|<
name|User
argument_list|>
name|subordinates
decl_stmt|;
DECL|field|superiors
specifier|private
name|List
argument_list|<
name|User
argument_list|>
name|superiors
decl_stmt|;
DECL|field|colleagues
specifier|private
name|List
argument_list|<
name|User
argument_list|>
name|colleagues
decl_stmt|;
DECL|method|getSubordinates ()
specifier|public
name|List
argument_list|<
name|User
argument_list|>
name|getSubordinates
parameter_list|()
block|{
return|return
name|subordinates
return|;
block|}
DECL|method|setSubordinates (List<User> subordinates)
specifier|public
name|void
name|setSubordinates
parameter_list|(
name|List
argument_list|<
name|User
argument_list|>
name|subordinates
parameter_list|)
block|{
name|this
operator|.
name|subordinates
operator|=
name|subordinates
expr_stmt|;
block|}
DECL|method|getSuperiors ()
specifier|public
name|List
argument_list|<
name|User
argument_list|>
name|getSuperiors
parameter_list|()
block|{
return|return
name|superiors
return|;
block|}
DECL|method|setSuperiors (List<User> superiors)
specifier|public
name|void
name|setSuperiors
parameter_list|(
name|List
argument_list|<
name|User
argument_list|>
name|superiors
parameter_list|)
block|{
name|this
operator|.
name|superiors
operator|=
name|superiors
expr_stmt|;
block|}
DECL|method|getColleagues ()
specifier|public
name|List
argument_list|<
name|User
argument_list|>
name|getColleagues
parameter_list|()
block|{
return|return
name|colleagues
return|;
block|}
DECL|method|setColleagues (List<User> colleagues)
specifier|public
name|void
name|setColleagues
parameter_list|(
name|List
argument_list|<
name|User
argument_list|>
name|colleagues
parameter_list|)
block|{
name|this
operator|.
name|colleagues
operator|=
name|colleagues
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Relationships [subordinates="
operator|+
name|subordinates
operator|+
literal|", superiors="
operator|+
name|superiors
operator|+
literal|", colleagues="
operator|+
name|colleagues
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

