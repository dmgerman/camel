begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|component
operator|.
name|properties
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
import|;
end_import

begin_comment
comment|/**  * Simple extension for properties component which uses custom property resolver.  */
end_comment

begin_class
annotation|@
name|Named
argument_list|(
literal|"properties"
argument_list|)
DECL|class|CdiPropertiesComponent
specifier|public
class|class
name|CdiPropertiesComponent
extends|extends
name|PropertiesComponent
block|{
DECL|method|CdiPropertiesComponent ()
specifier|public
name|CdiPropertiesComponent
parameter_list|()
block|{
name|setPropertiesParser
argument_list|(
operator|new
name|CdiPropertiesParser
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

