begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.snakeyaml.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|snakeyaml
operator|.
name|springboot
package|;
end_package

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
name|snakeyaml
operator|.
name|SnakeYAMLDataFormat
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
name|model
operator|.
name|dataformat
operator|.
name|YAMLLibrary
import|;
end_import

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

begin_comment
comment|/**  * Camel SnakeYAML support  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.dataformat.yaml-snakeyaml"
argument_list|)
DECL|class|SnakeYAMLDataFormatConfiguration
specifier|public
class|class
name|SnakeYAMLDataFormatConfiguration
block|{
comment|/**      * Which yaml library to use such. Is by default SnakeYAML      */
DECL|field|library
specifier|private
name|YAMLLibrary
name|library
init|=
name|YAMLLibrary
operator|.
name|SnakeYAML
decl_stmt|;
comment|/**      * Class name of the java type to use when unarmshalling      */
DECL|field|unmarshalTypeName
specifier|private
name|String
name|unmarshalTypeName
decl_stmt|;
comment|/**      * BaseConstructor to construct incoming documents.      */
DECL|field|constructor
specifier|private
name|String
name|constructor
decl_stmt|;
comment|/**      * Representer to emit outgoing objects.      */
DECL|field|representer
specifier|private
name|String
name|representer
decl_stmt|;
comment|/**      * DumperOptions to configure outgoing objects.      */
DECL|field|dumperOptions
specifier|private
name|String
name|dumperOptions
decl_stmt|;
comment|/**      * Resolver to detect implicit type      */
DECL|field|resolver
specifier|private
name|String
name|resolver
decl_stmt|;
comment|/**      * Use ApplicationContextClassLoader as custom ClassLoader      */
DECL|field|useApplicationContextClassLoader
specifier|private
name|Boolean
name|useApplicationContextClassLoader
init|=
literal|true
decl_stmt|;
comment|/**      * Force the emitter to produce a pretty YAML document when using the flow      * style.      */
DECL|field|prettyFlow
specifier|private
name|Boolean
name|prettyFlow
init|=
literal|false
decl_stmt|;
DECL|method|getLibrary ()
specifier|public
name|YAMLLibrary
name|getLibrary
parameter_list|()
block|{
return|return
name|library
return|;
block|}
DECL|method|setLibrary (YAMLLibrary library)
specifier|public
name|void
name|setLibrary
parameter_list|(
name|YAMLLibrary
name|library
parameter_list|)
block|{
name|this
operator|.
name|library
operator|=
name|library
expr_stmt|;
block|}
DECL|method|getUnmarshalTypeName ()
specifier|public
name|String
name|getUnmarshalTypeName
parameter_list|()
block|{
return|return
name|unmarshalTypeName
return|;
block|}
DECL|method|setUnmarshalTypeName (String unmarshalTypeName)
specifier|public
name|void
name|setUnmarshalTypeName
parameter_list|(
name|String
name|unmarshalTypeName
parameter_list|)
block|{
name|this
operator|.
name|unmarshalTypeName
operator|=
name|unmarshalTypeName
expr_stmt|;
block|}
DECL|method|getConstructor ()
specifier|public
name|String
name|getConstructor
parameter_list|()
block|{
return|return
name|constructor
return|;
block|}
DECL|method|setConstructor (String constructor)
specifier|public
name|void
name|setConstructor
parameter_list|(
name|String
name|constructor
parameter_list|)
block|{
name|this
operator|.
name|constructor
operator|=
name|constructor
expr_stmt|;
block|}
DECL|method|getRepresenter ()
specifier|public
name|String
name|getRepresenter
parameter_list|()
block|{
return|return
name|representer
return|;
block|}
DECL|method|setRepresenter (String representer)
specifier|public
name|void
name|setRepresenter
parameter_list|(
name|String
name|representer
parameter_list|)
block|{
name|this
operator|.
name|representer
operator|=
name|representer
expr_stmt|;
block|}
DECL|method|getDumperOptions ()
specifier|public
name|String
name|getDumperOptions
parameter_list|()
block|{
return|return
name|dumperOptions
return|;
block|}
DECL|method|setDumperOptions (String dumperOptions)
specifier|public
name|void
name|setDumperOptions
parameter_list|(
name|String
name|dumperOptions
parameter_list|)
block|{
name|this
operator|.
name|dumperOptions
operator|=
name|dumperOptions
expr_stmt|;
block|}
DECL|method|getResolver ()
specifier|public
name|String
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
DECL|method|setResolver (String resolver)
specifier|public
name|void
name|setResolver
parameter_list|(
name|String
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
block|}
DECL|method|getUseApplicationContextClassLoader ()
specifier|public
name|Boolean
name|getUseApplicationContextClassLoader
parameter_list|()
block|{
return|return
name|useApplicationContextClassLoader
return|;
block|}
DECL|method|setUseApplicationContextClassLoader ( Boolean useApplicationContextClassLoader)
specifier|public
name|void
name|setUseApplicationContextClassLoader
parameter_list|(
name|Boolean
name|useApplicationContextClassLoader
parameter_list|)
block|{
name|this
operator|.
name|useApplicationContextClassLoader
operator|=
name|useApplicationContextClassLoader
expr_stmt|;
block|}
DECL|method|getPrettyFlow ()
specifier|public
name|Boolean
name|getPrettyFlow
parameter_list|()
block|{
return|return
name|prettyFlow
return|;
block|}
DECL|method|setPrettyFlow (Boolean prettyFlow)
specifier|public
name|void
name|setPrettyFlow
parameter_list|(
name|Boolean
name|prettyFlow
parameter_list|)
block|{
name|this
operator|.
name|prettyFlow
operator|=
name|prettyFlow
expr_stmt|;
block|}
block|}
end_class

end_unit

