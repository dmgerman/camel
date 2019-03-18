begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flatpack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flatpack
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|DataSet
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|DefaultParserFactory
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|ParserFactory
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
name|Component
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
name|Consumer
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
name|Exchange
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
name|InvalidPayloadException
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
name|Message
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
name|Processor
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
name|Producer
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
name|processor
operator|.
name|loadbalancer
operator|.
name|LoadBalancer
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
name|processor
operator|.
name|loadbalancer
operator|.
name|RoundRobinLoadBalancer
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|support
operator|.
name|DefaultPollingEndpoint
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
name|support
operator|.
name|ExchangeHelper
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
name|support
operator|.
name|ResourceHelper
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
name|util
operator|.
name|IOHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The flatpack component supports fixed width and delimited file parsing via the FlatPack library.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.4.0"
argument_list|,
name|scheme
operator|=
literal|"flatpack"
argument_list|,
name|title
operator|=
literal|"Flatpack"
argument_list|,
name|syntax
operator|=
literal|"flatpack:type:resourceUri"
argument_list|,
name|label
operator|=
literal|"transformation"
argument_list|)
DECL|class|FlatpackEndpoint
specifier|public
class|class
name|FlatpackEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|loadBalancer
specifier|private
name|LoadBalancer
name|loadBalancer
init|=
operator|new
name|RoundRobinLoadBalancer
argument_list|()
decl_stmt|;
DECL|field|parserFactory
specifier|private
name|ParserFactory
name|parserFactory
init|=
name|DefaultParserFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|defaultValue
operator|=
literal|"delim"
argument_list|)
DECL|field|type
specifier|private
name|FlatpackType
name|type
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|splitRows
specifier|private
name|boolean
name|splitRows
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|allowShortLines
specifier|private
name|boolean
name|allowShortLines
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ignoreExtraColumns
specifier|private
name|boolean
name|ignoreExtraColumns
decl_stmt|;
comment|// delimited
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|","
argument_list|)
DECL|field|delimiter
specifier|private
name|char
name|delimiter
init|=
literal|','
decl_stmt|;
annotation|@
name|UriParam
DECL|field|textQualifier
specifier|private
name|char
name|textQualifier
init|=
literal|'"'
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|ignoreFirstRecord
specifier|private
name|boolean
name|ignoreFirstRecord
init|=
literal|true
decl_stmt|;
DECL|method|FlatpackEndpoint ()
specifier|public
name|FlatpackEndpoint
parameter_list|()
block|{     }
DECL|method|FlatpackEndpoint (String endpointUri, Component component, String resourceUri)
specifier|public
name|FlatpackEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|FlatpackProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|FlatpackConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|loadBalancer
argument_list|)
return|;
block|}
DECL|method|processDataSet (Exchange originalExchange, DataSet dataSet, int counter)
specifier|public
name|void
name|processDataSet
parameter_list|(
name|Exchange
name|originalExchange
parameter_list|,
name|DataSet
name|dataSet
parameter_list|,
name|int
name|counter
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|ExchangeHelper
operator|.
name|createCorrelatedCopy
argument_list|(
name|originalExchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|dataSet
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"CamelFlatpackCounter"
argument_list|,
name|counter
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|createParser (Exchange exchange)
specifier|public
name|Parser
name|createParser
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Reader
name|bodyReader
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|FlatpackType
operator|.
name|fixed
operator|==
name|type
condition|)
block|{
return|return
name|createFixedParser
argument_list|(
name|resourceUri
argument_list|,
name|bodyReader
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|createDelimitedParser
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// must close reader in case of some exception
name|IOHelper
operator|.
name|close
argument_list|(
name|bodyReader
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
DECL|method|createFixedParser (String resourceUri, Reader bodyReader)
specifier|protected
name|Parser
name|createFixedParser
parameter_list|(
name|String
name|resourceUri
parameter_list|,
name|Reader
name|bodyReader
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
decl_stmt|;
name|InputStreamReader
name|reader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Parser
name|parser
init|=
name|getParserFactory
argument_list|()
operator|.
name|newFixedLengthParser
argument_list|(
name|reader
argument_list|,
name|bodyReader
argument_list|)
decl_stmt|;
if|if
condition|(
name|isAllowShortLines
argument_list|()
condition|)
block|{
name|parser
operator|.
name|setHandlingShortLines
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setIgnoreParseWarnings
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isIgnoreExtraColumns
argument_list|()
condition|)
block|{
name|parser
operator|.
name|setIgnoreExtraColumns
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setIgnoreParseWarnings
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|parser
return|;
block|}
DECL|method|createDelimitedParser (Exchange exchange)
specifier|public
name|Parser
name|createDelimitedParser
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidPayloadException
throws|,
name|IOException
block|{
name|Reader
name|bodyReader
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
name|Parser
name|parser
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|getResourceUri
argument_list|()
argument_list|)
condition|)
block|{
name|parser
operator|=
name|getParserFactory
argument_list|()
operator|.
name|newDelimitedParser
argument_list|(
name|bodyReader
argument_list|,
name|delimiter
argument_list|,
name|textQualifier
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
decl_stmt|;
name|InputStreamReader
name|reader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|parser
operator|=
name|getParserFactory
argument_list|()
operator|.
name|newDelimitedParser
argument_list|(
name|reader
argument_list|,
name|bodyReader
argument_list|,
name|delimiter
argument_list|,
name|textQualifier
argument_list|,
name|ignoreFirstRecord
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isAllowShortLines
argument_list|()
condition|)
block|{
name|parser
operator|.
name|setHandlingShortLines
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setIgnoreParseWarnings
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isIgnoreExtraColumns
argument_list|()
condition|)
block|{
name|parser
operator|.
name|setIgnoreExtraColumns
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setIgnoreParseWarnings
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|parser
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getResourceUri ()
specifier|public
name|String
name|getResourceUri
parameter_list|()
block|{
return|return
name|resourceUri
return|;
block|}
DECL|method|getParserFactory ()
specifier|public
name|ParserFactory
name|getParserFactory
parameter_list|()
block|{
return|return
name|parserFactory
return|;
block|}
DECL|method|setParserFactory (ParserFactory parserFactory)
specifier|public
name|void
name|setParserFactory
parameter_list|(
name|ParserFactory
name|parserFactory
parameter_list|)
block|{
name|this
operator|.
name|parserFactory
operator|=
name|parserFactory
expr_stmt|;
block|}
DECL|method|getLoadBalancer ()
specifier|public
name|LoadBalancer
name|getLoadBalancer
parameter_list|()
block|{
return|return
name|loadBalancer
return|;
block|}
DECL|method|setLoadBalancer (LoadBalancer loadBalancer)
specifier|public
name|void
name|setLoadBalancer
parameter_list|(
name|LoadBalancer
name|loadBalancer
parameter_list|)
block|{
name|this
operator|.
name|loadBalancer
operator|=
name|loadBalancer
expr_stmt|;
block|}
DECL|method|isSplitRows ()
specifier|public
name|boolean
name|isSplitRows
parameter_list|()
block|{
return|return
name|splitRows
return|;
block|}
comment|/**      * Sets the Component to send each row as a separate exchange once parsed      */
DECL|method|setSplitRows (boolean splitRows)
specifier|public
name|void
name|setSplitRows
parameter_list|(
name|boolean
name|splitRows
parameter_list|)
block|{
name|this
operator|.
name|splitRows
operator|=
name|splitRows
expr_stmt|;
block|}
DECL|method|isAllowShortLines ()
specifier|public
name|boolean
name|isAllowShortLines
parameter_list|()
block|{
return|return
name|this
operator|.
name|allowShortLines
return|;
block|}
comment|/**      * Allows for lines to be shorter than expected and ignores the extra characters      */
DECL|method|setAllowShortLines (boolean allowShortLines)
specifier|public
name|void
name|setAllowShortLines
parameter_list|(
name|boolean
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
comment|/**      * Allows for lines to be longer than expected and ignores the extra characters      */
DECL|method|setIgnoreExtraColumns (boolean ignoreExtraColumns)
specifier|public
name|void
name|setIgnoreExtraColumns
parameter_list|(
name|boolean
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
DECL|method|isIgnoreExtraColumns ()
specifier|public
name|boolean
name|isIgnoreExtraColumns
parameter_list|()
block|{
return|return
name|ignoreExtraColumns
return|;
block|}
DECL|method|getType ()
specifier|public
name|FlatpackType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Whether to use fixed or delimiter      */
DECL|method|setType (FlatpackType type)
specifier|public
name|void
name|setType
parameter_list|(
name|FlatpackType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * URL for loading the flatpack mapping file from classpath or file system      */
DECL|method|setResourceUri (String resourceUri)
specifier|public
name|void
name|setResourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
DECL|method|getDelimiter ()
specifier|public
name|char
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
comment|/**      * The default character delimiter for delimited files.      */
DECL|method|setDelimiter (char delimiter)
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|char
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
DECL|method|getTextQualifier ()
specifier|public
name|char
name|getTextQualifier
parameter_list|()
block|{
return|return
name|textQualifier
return|;
block|}
comment|/**      * The text qualifier for delimited files.      */
DECL|method|setTextQualifier (char textQualifier)
specifier|public
name|void
name|setTextQualifier
parameter_list|(
name|char
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
DECL|method|isIgnoreFirstRecord ()
specifier|public
name|boolean
name|isIgnoreFirstRecord
parameter_list|()
block|{
return|return
name|ignoreFirstRecord
return|;
block|}
comment|/**      * Whether the first line is ignored for delimited files (for the column headers).      */
DECL|method|setIgnoreFirstRecord (boolean ignoreFirstRecord)
specifier|public
name|void
name|setIgnoreFirstRecord
parameter_list|(
name|boolean
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
block|}
end_class

end_unit

