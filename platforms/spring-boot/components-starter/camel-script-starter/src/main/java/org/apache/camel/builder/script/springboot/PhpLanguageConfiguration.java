begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.script.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|script
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|LanguageConfigurationPropertiesCommon
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
comment|/**  * To use PHP scripts in Camel expressions or predicates.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.language.php"
argument_list|)
DECL|class|PhpLanguageConfiguration
specifier|public
class|class
name|PhpLanguageConfiguration
extends|extends
name|LanguageConfigurationPropertiesCommon
block|{
comment|/**      * Whether to trim the value to remove leading and trailing whitespaces and      * line breaks      */
DECL|field|trim
specifier|private
name|Boolean
name|trim
init|=
literal|true
decl_stmt|;
DECL|method|getTrim ()
specifier|public
name|Boolean
name|getTrim
parameter_list|()
block|{
return|return
name|trim
return|;
block|}
DECL|method|setTrim (Boolean trim)
specifier|public
name|void
name|setTrim
parameter_list|(
name|Boolean
name|trim
parameter_list|)
block|{
name|this
operator|.
name|trim
operator|=
name|trim
expr_stmt|;
block|}
block|}
end_class

end_unit

