begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_class
DECL|class|LanguageConfigurationPropertiesCommon
specifier|public
class|class
name|LanguageConfigurationPropertiesCommon
block|{
comment|/**      * Global option to enable/disable language auto-configuration, default is true.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
annotation|@
name|NestedConfigurationProperty
DECL|field|customizer
specifier|private
name|CustomizerProperties
name|customizer
init|=
operator|new
name|CustomizerProperties
argument_list|()
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getCustomizer ()
specifier|public
name|CustomizerProperties
name|getCustomizer
parameter_list|()
block|{
return|return
name|customizer
return|;
block|}
comment|// ********************************
comment|// Customizer
comment|// ********************************
DECL|class|CustomizerProperties
specifier|public
class|class
name|CustomizerProperties
block|{
comment|/**          * Global option to enable/disable language customizers, default is true.          */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

