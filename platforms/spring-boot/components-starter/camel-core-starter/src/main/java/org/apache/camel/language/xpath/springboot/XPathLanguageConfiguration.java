begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.xpath.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|xpath
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
comment|/**  * For XPath expressions and predicates  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.language.xpath"
argument_list|)
DECL|class|XPathLanguageConfiguration
specifier|public
class|class
name|XPathLanguageConfiguration
extends|extends
name|LanguageConfigurationPropertiesCommon
block|{
comment|/**      * Name of class for document type The default value is org.w3c.dom.Document      */
DECL|field|documentType
specifier|private
name|String
name|documentType
decl_stmt|;
comment|/**      * Whether to use Saxon.      */
DECL|field|saxon
specifier|private
name|Boolean
name|saxon
init|=
literal|false
decl_stmt|;
comment|/**      * References to a custom XPathFactory to lookup in the registry      */
DECL|field|factoryRef
specifier|private
name|String
name|factoryRef
decl_stmt|;
comment|/**      * The XPath object model to use      */
DECL|field|objectModel
specifier|private
name|String
name|objectModel
decl_stmt|;
comment|/**      * Whether to log namespaces which can assist during trouble shooting      */
DECL|field|logNamespaces
specifier|private
name|Boolean
name|logNamespaces
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to enable thread-safety for the returned result of the xpath      * expression. This applies to when using NODESET as the result type and the      * returned set has multiple elements. In this situation there can be      * thread-safety issues if you process the NODESET concurrently such as from      * a Camel Splitter EIP in parallel processing mode. This option prevents      * concurrency issues by doing defensive copies of the nodes. It is      * recommended to turn this option on if you are using camel-saxon or Saxon      * in your application. Saxon has thread-safety issues which can be      * prevented by turning this option on.      */
DECL|field|threadSafety
specifier|private
name|Boolean
name|threadSafety
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to trim the value to remove leading and trailing whitespaces and      * line breaks      */
DECL|field|trim
specifier|private
name|Boolean
name|trim
init|=
literal|true
decl_stmt|;
DECL|method|getDocumentType ()
specifier|public
name|String
name|getDocumentType
parameter_list|()
block|{
return|return
name|documentType
return|;
block|}
DECL|method|setDocumentType (String documentType)
specifier|public
name|void
name|setDocumentType
parameter_list|(
name|String
name|documentType
parameter_list|)
block|{
name|this
operator|.
name|documentType
operator|=
name|documentType
expr_stmt|;
block|}
DECL|method|getSaxon ()
specifier|public
name|Boolean
name|getSaxon
parameter_list|()
block|{
return|return
name|saxon
return|;
block|}
DECL|method|setSaxon (Boolean saxon)
specifier|public
name|void
name|setSaxon
parameter_list|(
name|Boolean
name|saxon
parameter_list|)
block|{
name|this
operator|.
name|saxon
operator|=
name|saxon
expr_stmt|;
block|}
DECL|method|getFactoryRef ()
specifier|public
name|String
name|getFactoryRef
parameter_list|()
block|{
return|return
name|factoryRef
return|;
block|}
DECL|method|setFactoryRef (String factoryRef)
specifier|public
name|void
name|setFactoryRef
parameter_list|(
name|String
name|factoryRef
parameter_list|)
block|{
name|this
operator|.
name|factoryRef
operator|=
name|factoryRef
expr_stmt|;
block|}
DECL|method|getObjectModel ()
specifier|public
name|String
name|getObjectModel
parameter_list|()
block|{
return|return
name|objectModel
return|;
block|}
DECL|method|setObjectModel (String objectModel)
specifier|public
name|void
name|setObjectModel
parameter_list|(
name|String
name|objectModel
parameter_list|)
block|{
name|this
operator|.
name|objectModel
operator|=
name|objectModel
expr_stmt|;
block|}
DECL|method|getLogNamespaces ()
specifier|public
name|Boolean
name|getLogNamespaces
parameter_list|()
block|{
return|return
name|logNamespaces
return|;
block|}
DECL|method|setLogNamespaces (Boolean logNamespaces)
specifier|public
name|void
name|setLogNamespaces
parameter_list|(
name|Boolean
name|logNamespaces
parameter_list|)
block|{
name|this
operator|.
name|logNamespaces
operator|=
name|logNamespaces
expr_stmt|;
block|}
DECL|method|getThreadSafety ()
specifier|public
name|Boolean
name|getThreadSafety
parameter_list|()
block|{
return|return
name|threadSafety
return|;
block|}
DECL|method|setThreadSafety (Boolean threadSafety)
specifier|public
name|void
name|setThreadSafety
parameter_list|(
name|Boolean
name|threadSafety
parameter_list|)
block|{
name|this
operator|.
name|threadSafety
operator|=
name|threadSafety
expr_stmt|;
block|}
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

