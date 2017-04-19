begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.flatpack.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|flatpack
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
comment|/**  * Camel FlatPack support  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.dataformat.flatpack"
argument_list|)
DECL|class|FlatpackDataFormatConfiguration
specifier|public
class|class
name|FlatpackDataFormatConfiguration
block|{
comment|/**      * References to a custom parser factory to lookup in the registry      */
DECL|field|parserFactoryRef
specifier|private
name|String
name|parserFactoryRef
decl_stmt|;
comment|/**      * The flatpack pzmap configuration file. Can be omitted in simpler      * situations but its preferred to use the pzmap.      */
DECL|field|definition
specifier|private
name|String
name|definition
decl_stmt|;
comment|/**      * Delimited or fixed. Is by default false = delimited      */
DECL|field|fixed
specifier|private
name|Boolean
name|fixed
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the first line is ignored for delimited files (for the column      * headers). Is by default true.      */
DECL|field|ignoreFirstRecord
specifier|private
name|Boolean
name|ignoreFirstRecord
init|=
literal|true
decl_stmt|;
comment|/**      * If the text is qualified with a char such as "      */
DECL|field|textQualifier
specifier|private
name|String
name|textQualifier
decl_stmt|;
comment|/**      * The delimiter char (could be ; or similar)      */
DECL|field|delimiter
specifier|private
name|String
name|delimiter
decl_stmt|;
comment|/**      * Allows for lines to be shorter than expected and ignores the extra      * characters      */
DECL|field|allowShortLines
specifier|private
name|Boolean
name|allowShortLines
init|=
literal|false
decl_stmt|;
comment|/**      * Allows for lines to be longer than expected and ignores the extra      * characters.      */
DECL|field|ignoreExtraColumns
specifier|private
name|Boolean
name|ignoreExtraColumns
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getParserFactoryRef ()
specifier|public
name|String
name|getParserFactoryRef
parameter_list|()
block|{
return|return
name|parserFactoryRef
return|;
block|}
DECL|method|setParserFactoryRef (String parserFactoryRef)
specifier|public
name|void
name|setParserFactoryRef
parameter_list|(
name|String
name|parserFactoryRef
parameter_list|)
block|{
name|this
operator|.
name|parserFactoryRef
operator|=
name|parserFactoryRef
expr_stmt|;
block|}
DECL|method|getDefinition ()
specifier|public
name|String
name|getDefinition
parameter_list|()
block|{
return|return
name|definition
return|;
block|}
DECL|method|setDefinition (String definition)
specifier|public
name|void
name|setDefinition
parameter_list|(
name|String
name|definition
parameter_list|)
block|{
name|this
operator|.
name|definition
operator|=
name|definition
expr_stmt|;
block|}
DECL|method|getFixed ()
specifier|public
name|Boolean
name|getFixed
parameter_list|()
block|{
return|return
name|fixed
return|;
block|}
DECL|method|setFixed (Boolean fixed)
specifier|public
name|void
name|setFixed
parameter_list|(
name|Boolean
name|fixed
parameter_list|)
block|{
name|this
operator|.
name|fixed
operator|=
name|fixed
expr_stmt|;
block|}
DECL|method|getIgnoreFirstRecord ()
specifier|public
name|Boolean
name|getIgnoreFirstRecord
parameter_list|()
block|{
return|return
name|ignoreFirstRecord
return|;
block|}
DECL|method|setIgnoreFirstRecord (Boolean ignoreFirstRecord)
specifier|public
name|void
name|setIgnoreFirstRecord
parameter_list|(
name|Boolean
name|ignoreFirstRecord
parameter_list|)
block|{
name|this
operator|.
name|ignoreFirstRecord
operator|=
name|ignoreFirstRecord
expr_stmt|;
block|}
DECL|method|getTextQualifier ()
specifier|public
name|String
name|getTextQualifier
parameter_list|()
block|{
return|return
name|textQualifier
return|;
block|}
DECL|method|setTextQualifier (String textQualifier)
specifier|public
name|void
name|setTextQualifier
parameter_list|(
name|String
name|textQualifier
parameter_list|)
block|{
name|this
operator|.
name|textQualifier
operator|=
name|textQualifier
expr_stmt|;
block|}
DECL|method|getDelimiter ()
specifier|public
name|String
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
DECL|method|setDelimiter (String delimiter)
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|String
name|delimiter
parameter_list|)
block|{
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
DECL|method|getAllowShortLines ()
specifier|public
name|Boolean
name|getAllowShortLines
parameter_list|()
block|{
return|return
name|allowShortLines
return|;
block|}
DECL|method|setAllowShortLines (Boolean allowShortLines)
specifier|public
name|void
name|setAllowShortLines
parameter_list|(
name|Boolean
name|allowShortLines
parameter_list|)
block|{
name|this
operator|.
name|allowShortLines
operator|=
name|allowShortLines
expr_stmt|;
block|}
DECL|method|getIgnoreExtraColumns ()
specifier|public
name|Boolean
name|getIgnoreExtraColumns
parameter_list|()
block|{
return|return
name|ignoreExtraColumns
return|;
block|}
DECL|method|setIgnoreExtraColumns (Boolean ignoreExtraColumns)
specifier|public
name|void
name|setIgnoreExtraColumns
parameter_list|(
name|Boolean
name|ignoreExtraColumns
parameter_list|)
block|{
name|this
operator|.
name|ignoreExtraColumns
operator|=
name|ignoreExtraColumns
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

