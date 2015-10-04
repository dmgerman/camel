begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jacksonxml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jacksonxml
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
name|core
operator|.
name|Version
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
name|databind
operator|.
name|Module
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
name|databind
operator|.
name|PropertyNamingStrategy
import|;
end_import

begin_class
DECL|class|MyModule
specifier|public
class|class
name|MyModule
extends|extends
name|Module
block|{
annotation|@
name|Override
DECL|method|getModuleName ()
specifier|public
name|String
name|getModuleName
parameter_list|()
block|{
return|return
literal|"MyModule"
return|;
block|}
annotation|@
name|Override
DECL|method|version ()
specifier|public
name|Version
name|version
parameter_list|()
block|{
return|return
name|Version
operator|.
name|unknownVersion
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setupModule (SetupContext context)
specifier|public
name|void
name|setupModule
parameter_list|(
name|SetupContext
name|context
parameter_list|)
block|{
name|context
operator|.
name|setNamingStrategy
argument_list|(
operator|new
name|PropertyNamingStrategy
operator|.
name|PropertyNamingStrategyBase
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|translate
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
return|return
literal|"my-"
operator|+
name|propertyName
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

