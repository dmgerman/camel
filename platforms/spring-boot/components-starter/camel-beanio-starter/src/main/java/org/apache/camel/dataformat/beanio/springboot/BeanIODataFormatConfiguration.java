begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.beanio.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|beanio
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
comment|/**  * Camel BeanIO data format support  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.dataformat.beanio"
argument_list|)
DECL|class|BeanIODataFormatConfiguration
specifier|public
class|class
name|BeanIODataFormatConfiguration
block|{
comment|/**      * The BeanIO mapping file. Is by default loaded from the classpath. You can      * prefix with file: http: or classpath: to denote from where to load the      * mapping file.      */
DECL|field|mapping
specifier|private
name|String
name|mapping
decl_stmt|;
comment|/**      * The name of the stream to use.      */
DECL|field|streamName
specifier|private
name|String
name|streamName
decl_stmt|;
comment|/**      * Whether to ignore unidentified records.      */
DECL|field|ignoreUnidentifiedRecords
specifier|private
name|Boolean
name|ignoreUnidentifiedRecords
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to ignore unexpected records.      */
DECL|field|ignoreUnexpectedRecords
specifier|private
name|Boolean
name|ignoreUnexpectedRecords
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to ignore invalid records.      */
DECL|field|ignoreInvalidRecords
specifier|private
name|Boolean
name|ignoreInvalidRecords
init|=
literal|false
decl_stmt|;
comment|/**      * The charset to use. Is by default the JVM platform default charset.      */
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.dataformat.beanio.BeanIOErrorHandler as      * error handler while parsing. Configure the fully qualified class name of      * the error handler. Notice the options ignoreUnidentifiedRecords      * ignoreUnexpectedRecords and ignoreInvalidRecords may not be in use when      * you use a custom error handler.      */
DECL|field|beanReaderErrorHandlerType
specifier|private
name|String
name|beanReaderErrorHandlerType
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getMapping ()
specifier|public
name|String
name|getMapping
parameter_list|()
block|{
return|return
name|mapping
return|;
block|}
DECL|method|setMapping (String mapping)
specifier|public
name|void
name|setMapping
parameter_list|(
name|String
name|mapping
parameter_list|)
block|{
name|this
operator|.
name|mapping
operator|=
name|mapping
expr_stmt|;
block|}
DECL|method|getStreamName ()
specifier|public
name|String
name|getStreamName
parameter_list|()
block|{
return|return
name|streamName
return|;
block|}
DECL|method|setStreamName (String streamName)
specifier|public
name|void
name|setStreamName
parameter_list|(
name|String
name|streamName
parameter_list|)
block|{
name|this
operator|.
name|streamName
operator|=
name|streamName
expr_stmt|;
block|}
DECL|method|getIgnoreUnidentifiedRecords ()
specifier|public
name|Boolean
name|getIgnoreUnidentifiedRecords
parameter_list|()
block|{
return|return
name|ignoreUnidentifiedRecords
return|;
block|}
DECL|method|setIgnoreUnidentifiedRecords (Boolean ignoreUnidentifiedRecords)
specifier|public
name|void
name|setIgnoreUnidentifiedRecords
parameter_list|(
name|Boolean
name|ignoreUnidentifiedRecords
parameter_list|)
block|{
name|this
operator|.
name|ignoreUnidentifiedRecords
operator|=
name|ignoreUnidentifiedRecords
expr_stmt|;
block|}
DECL|method|getIgnoreUnexpectedRecords ()
specifier|public
name|Boolean
name|getIgnoreUnexpectedRecords
parameter_list|()
block|{
return|return
name|ignoreUnexpectedRecords
return|;
block|}
DECL|method|setIgnoreUnexpectedRecords (Boolean ignoreUnexpectedRecords)
specifier|public
name|void
name|setIgnoreUnexpectedRecords
parameter_list|(
name|Boolean
name|ignoreUnexpectedRecords
parameter_list|)
block|{
name|this
operator|.
name|ignoreUnexpectedRecords
operator|=
name|ignoreUnexpectedRecords
expr_stmt|;
block|}
DECL|method|getIgnoreInvalidRecords ()
specifier|public
name|Boolean
name|getIgnoreInvalidRecords
parameter_list|()
block|{
return|return
name|ignoreInvalidRecords
return|;
block|}
DECL|method|setIgnoreInvalidRecords (Boolean ignoreInvalidRecords)
specifier|public
name|void
name|setIgnoreInvalidRecords
parameter_list|(
name|Boolean
name|ignoreInvalidRecords
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidRecords
operator|=
name|ignoreInvalidRecords
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
DECL|method|getBeanReaderErrorHandlerType ()
specifier|public
name|String
name|getBeanReaderErrorHandlerType
parameter_list|()
block|{
return|return
name|beanReaderErrorHandlerType
return|;
block|}
DECL|method|setBeanReaderErrorHandlerType (String beanReaderErrorHandlerType)
specifier|public
name|void
name|setBeanReaderErrorHandlerType
parameter_list|(
name|String
name|beanReaderErrorHandlerType
parameter_list|)
block|{
name|this
operator|.
name|beanReaderErrorHandlerType
operator|=
name|beanReaderErrorHandlerType
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

