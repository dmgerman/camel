begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.castor.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|castor
operator|.
name|springboot
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

begin_comment
comment|/**  * Camel Castor data format support  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.dataformat.castor"
argument_list|)
DECL|class|CastorDataFormatConfiguration
specifier|public
class|class
name|CastorDataFormatConfiguration
block|{
comment|/**      * Path to a Castor mapping file to load from the classpath.      */
DECL|field|mappingFile
specifier|private
name|String
name|mappingFile
decl_stmt|;
comment|/**      * Whether validation is turned on or off. Is by default true.      */
DECL|field|validation
specifier|private
name|Boolean
name|validation
init|=
literal|true
decl_stmt|;
comment|/**      * Encoding to use when marshalling an Object to XML. Is by default UTF-8      */
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
comment|/**      * Add additional packages to Castor XmlContext      */
DECL|field|packages
specifier|private
name|String
index|[]
name|packages
decl_stmt|;
comment|/**      * Add additional class names to Castor XmlContext      */
DECL|field|classes
specifier|private
name|String
index|[]
name|classes
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getMappingFile ()
specifier|public
name|String
name|getMappingFile
parameter_list|()
block|{
return|return
name|mappingFile
return|;
block|}
DECL|method|setMappingFile (String mappingFile)
specifier|public
name|void
name|setMappingFile
parameter_list|(
name|String
name|mappingFile
parameter_list|)
block|{
name|this
operator|.
name|mappingFile
operator|=
name|mappingFile
expr_stmt|;
block|}
DECL|method|getValidation ()
specifier|public
name|Boolean
name|getValidation
parameter_list|()
block|{
return|return
name|validation
return|;
block|}
DECL|method|setValidation (Boolean validation)
specifier|public
name|void
name|setValidation
parameter_list|(
name|Boolean
name|validation
parameter_list|)
block|{
name|this
operator|.
name|validation
operator|=
name|validation
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|getPackages ()
specifier|public
name|String
index|[]
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
DECL|method|setPackages (String[] packages)
specifier|public
name|void
name|setPackages
parameter_list|(
name|String
index|[]
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
DECL|method|getClasses ()
specifier|public
name|String
index|[]
name|getClasses
parameter_list|()
block|{
return|return
name|classes
return|;
block|}
DECL|method|setClasses (String[] classes)
specifier|public
name|void
name|setClasses
parameter_list|(
name|String
index|[]
name|classes
parameter_list|)
block|{
name|this
operator|.
name|classes
operator|=
name|classes
expr_stmt|;
block|}
DECL|method|getContentTypeHeader ()
specifier|public
name|Boolean
name|getContentTypeHeader
parameter_list|()
block|{
return|return
name|contentTypeHeader
return|;
block|}
DECL|method|setContentTypeHeader (Boolean contentTypeHeader)
specifier|public
name|void
name|setContentTypeHeader
parameter_list|(
name|Boolean
name|contentTypeHeader
parameter_list|)
block|{
name|this
operator|.
name|contentTypeHeader
operator|=
name|contentTypeHeader
expr_stmt|;
block|}
block|}
end_class

end_unit

