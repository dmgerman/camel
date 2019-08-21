begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.any23
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|any23
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|any23
operator|.
name|Any23
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|any23
operator|.
name|configuration
operator|.
name|DefaultConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|any23
operator|.
name|configuration
operator|.
name|ModifiableConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|any23
operator|.
name|source
operator|.
name|ByteArrayDocumentSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|any23
operator|.
name|writer
operator|.
name|TripleHandler
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
name|dataformat
operator|.
name|any23
operator|.
name|utils
operator|.
name|Any23Utils
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
name|dataformat
operator|.
name|any23
operator|.
name|writer
operator|.
name|RDF4JModelWriter
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
name|DataFormat
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
name|DataFormatName
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
name|annotations
operator|.
name|Dataformat
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
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|rdf4j
operator|.
name|model
operator|.
name|Model
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|rdf4j
operator|.
name|rio
operator|.
name|RDFFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|rdf4j
operator|.
name|rio
operator|.
name|Rio
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Dataformat for any23 .. This dataformat is intended to convert HTML from a  * site (or file) into rdf.  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"any23"
argument_list|)
DECL|class|Any23DataFormat
specifier|public
class|class
name|Any23DataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
DECL|field|any23
specifier|private
name|Any23
name|any23
decl_stmt|;
DECL|field|configurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|configurations
decl_stmt|;
DECL|field|extractors
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|extractors
decl_stmt|;
DECL|field|outputFormat
specifier|private
name|Any23OutputFormat
name|outputFormat
decl_stmt|;
DECL|field|baseURI
specifier|private
name|String
name|baseURI
decl_stmt|;
DECL|method|Any23DataFormat ()
specifier|public
name|Any23DataFormat
parameter_list|()
block|{     }
DECL|method|Any23DataFormat (String baseURI)
specifier|public
name|Any23DataFormat
parameter_list|(
name|String
name|baseURI
parameter_list|)
block|{
name|this
operator|.
name|baseURI
operator|=
name|baseURI
expr_stmt|;
block|}
DECL|method|Any23DataFormat (Any23OutputFormat outputFormat, String baseURI)
specifier|public
name|Any23DataFormat
parameter_list|(
name|Any23OutputFormat
name|outputFormat
parameter_list|,
name|String
name|baseURI
parameter_list|)
block|{
name|this
operator|.
name|outputFormat
operator|=
name|outputFormat
expr_stmt|;
name|this
operator|.
name|baseURI
operator|=
name|baseURI
expr_stmt|;
block|}
DECL|method|Any23DataFormat (Map<String, String> configurations, Any23OutputFormat outputFormat, String baseURI)
specifier|public
name|Any23DataFormat
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|configurations
parameter_list|,
name|Any23OutputFormat
name|outputFormat
parameter_list|,
name|String
name|baseURI
parameter_list|)
block|{
name|this
operator|.
name|configurations
operator|=
name|configurations
expr_stmt|;
name|this
operator|.
name|outputFormat
operator|=
name|outputFormat
expr_stmt|;
name|this
operator|.
name|baseURI
operator|=
name|baseURI
expr_stmt|;
block|}
DECL|method|Any23DataFormat (Map<String, String> configurations, List<String> extractors, Any23OutputFormat outputFormat, String baseURI)
specifier|public
name|Any23DataFormat
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|configurations
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|extractors
parameter_list|,
name|Any23OutputFormat
name|outputFormat
parameter_list|,
name|String
name|baseURI
parameter_list|)
block|{
name|this
operator|.
name|configurations
operator|=
name|configurations
expr_stmt|;
name|this
operator|.
name|extractors
operator|=
name|extractors
expr_stmt|;
name|this
operator|.
name|outputFormat
operator|=
name|outputFormat
expr_stmt|;
name|this
operator|.
name|baseURI
operator|=
name|baseURI
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"any23"
return|;
block|}
comment|/**      * Marshal data. Generate RDF.      */
DECL|method|marshal (Exchange exchange, Object object, OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|object
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|OutputStreamWriter
name|outputStreamWriter
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|outputStream
argument_list|)
decl_stmt|;
name|outputStreamWriter
operator|.
name|write
argument_list|(
literal|"<html><script type=\"application/ld+json\">\n"
argument_list|)
expr_stmt|;
name|outputStreamWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|Model
name|mdl
init|=
operator|(
name|Model
operator|)
name|object
decl_stmt|;
name|Rio
operator|.
name|write
argument_list|(
name|mdl
argument_list|,
name|outputStream
argument_list|,
name|RDFFormat
operator|.
name|JSONLD
argument_list|)
expr_stmt|;
name|outputStreamWriter
operator|.
name|write
argument_list|(
literal|"\n</script></html>"
argument_list|)
expr_stmt|;
name|outputStreamWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|outputStreamWriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|outputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Unmarshal the data      */
DECL|method|unmarshal (Exchange exchange, InputStream inputStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteArrayDocumentSource
name|source
init|=
operator|new
name|ByteArrayDocumentSource
argument_list|(
name|inputStream
argument_list|,
name|this
operator|.
name|baseURI
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|TripleHandler
name|handler
init|=
name|Any23Utils
operator|.
name|obtainHandler
argument_list|(
name|outputFormat
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|any23
operator|.
name|extract
argument_list|(
name|source
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|handler
operator|.
name|close
argument_list|()
expr_stmt|;
name|Object
name|respon
decl_stmt|;
if|if
condition|(
name|outputFormat
operator|==
name|Any23OutputFormat
operator|.
name|RDF4JMODEL
condition|)
block|{
name|respon
operator|=
operator|(
operator|(
name|RDF4JModelWriter
operator|)
name|handler
operator|)
operator|.
name|getModel
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|respon
operator|=
operator|new
name|String
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|respon
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ModifiableConfiguration
name|conf
init|=
literal|null
decl_stmt|;
name|String
index|[]
name|extrArray
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|extractors
operator|!=
literal|null
operator|&&
operator|!
name|extractors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|extrArray
operator|=
operator|new
name|String
index|[
name|extractors
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
name|extrArray
operator|=
name|extractors
operator|.
name|toArray
argument_list|(
name|extrArray
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurations
operator|!=
literal|null
operator|&&
operator|!
name|configurations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|conf
operator|=
name|DefaultConfiguration
operator|.
name|copy
argument_list|()
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|configurations
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|conf
operator|.
name|setProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|outputFormat
operator|==
literal|null
condition|)
block|{
comment|// Default output format
name|outputFormat
operator|=
name|Any23OutputFormat
operator|.
name|RDF4JMODEL
expr_stmt|;
block|}
if|if
condition|(
name|conf
operator|==
literal|null
operator|&&
name|extrArray
operator|==
literal|null
condition|)
block|{
name|any23
operator|=
operator|new
name|Any23
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|conf
operator|!=
literal|null
operator|&&
name|extrArray
operator|==
literal|null
condition|)
block|{
name|any23
operator|=
operator|new
name|Any23
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|conf
operator|==
literal|null
operator|&&
name|extrArray
operator|!=
literal|null
condition|)
block|{
name|any23
operator|=
operator|new
name|Any23
argument_list|(
name|extrArray
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|conf
operator|!=
literal|null
operator|&&
name|extrArray
operator|!=
literal|null
condition|)
block|{
name|any23
operator|=
operator|new
name|Any23
argument_list|(
name|conf
argument_list|,
name|extrArray
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|getAny23 ()
specifier|public
name|Any23
name|getAny23
parameter_list|()
block|{
return|return
name|any23
return|;
block|}
DECL|method|setAny23 (Any23 any23)
specifier|public
name|Any23DataFormat
name|setAny23
parameter_list|(
name|Any23
name|any23
parameter_list|)
block|{
name|this
operator|.
name|any23
operator|=
name|any23
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getConfigurations ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getConfigurations
parameter_list|()
block|{
return|return
name|configurations
return|;
block|}
DECL|method|setConfigurations (Map<String, String> configurations)
specifier|public
name|Any23DataFormat
name|setConfigurations
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|configurations
parameter_list|)
block|{
name|this
operator|.
name|configurations
operator|=
name|configurations
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getExtractors ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExtractors
parameter_list|()
block|{
return|return
name|extractors
return|;
block|}
DECL|method|setExtractors (List<String> extractors)
specifier|public
name|Any23DataFormat
name|setExtractors
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|extractors
parameter_list|)
block|{
name|this
operator|.
name|extractors
operator|=
name|extractors
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getOutputFormat ()
specifier|public
name|Any23OutputFormat
name|getOutputFormat
parameter_list|()
block|{
return|return
name|outputFormat
return|;
block|}
DECL|method|setOutputFormat (Any23OutputFormat outputFormat)
specifier|public
name|Any23DataFormat
name|setOutputFormat
parameter_list|(
name|Any23OutputFormat
name|outputFormat
parameter_list|)
block|{
name|this
operator|.
name|outputFormat
operator|=
name|outputFormat
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getBaseURI ()
specifier|public
name|String
name|getBaseURI
parameter_list|()
block|{
return|return
name|baseURI
return|;
block|}
DECL|method|setBaseURI (String baseURI)
specifier|public
name|Any23DataFormat
name|setBaseURI
parameter_list|(
name|String
name|baseURI
parameter_list|)
block|{
name|this
operator|.
name|baseURI
operator|=
name|baseURI
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

