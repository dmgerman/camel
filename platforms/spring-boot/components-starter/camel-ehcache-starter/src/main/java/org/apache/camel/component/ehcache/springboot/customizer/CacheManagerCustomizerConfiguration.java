begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ehcache.springboot.customizer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ehcache
operator|.
name|springboot
operator|.
name|customizer
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
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.ehcache.customizer.cache-manager"
argument_list|)
DECL|class|CacheManagerCustomizerConfiguration
specifier|public
class|class
name|CacheManagerCustomizerConfiguration
block|{
comment|/**      * Enable or disable the cache-manager customizer.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
comment|/**      * Configure if the cache manager eventually set on the component should be overridden by the customizer.      */
DECL|field|override
specifier|private
name|boolean
name|override
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
DECL|method|isOverride ()
specifier|public
name|boolean
name|isOverride
parameter_list|()
block|{
return|return
name|override
return|;
block|}
DECL|method|setOverride (boolean override)
specifier|public
name|void
name|setOverride
parameter_list|(
name|boolean
name|override
parameter_list|)
block|{
name|this
operator|.
name|override
operator|=
name|override
expr_stmt|;
block|}
block|}
end_class

end_unit

