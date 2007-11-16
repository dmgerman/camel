begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|ExpectedBodyTypeException
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
name|RuntimeTransformException
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * Creates a<a href="http://activemq.apache.org/camel/processor.html">Processor</a>  * which performs an XSLT transformation of the IN message body  *   * @version $Revision: 531854 $  */
end_comment

begin_class
DECL|class|XsltBuilder
specifier|public
class|class
name|XsltBuilder
implements|implements
name|Processor
block|{
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|converter
specifier|private
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
DECL|field|transformer
specifier|private
name|Transformer
name|transformer
decl_stmt|;
DECL|field|resultHandlerFactory
specifier|private
name|ResultHandlerFactory
name|resultHandlerFactory
init|=
operator|new
name|StringResultHandlerFactory
argument_list|()
decl_stmt|;
DECL|field|failOnNullBody
specifier|private
name|boolean
name|failOnNullBody
init|=
literal|true
decl_stmt|;
DECL|method|XsltBuilder ()
specifier|public
name|XsltBuilder
parameter_list|()
block|{     }
DECL|method|XsltBuilder (Transformer transformer)
specifier|public
name|XsltBuilder
parameter_list|(
name|Transformer
name|transformer
parameter_list|)
block|{
name|this
operator|.
name|transformer
operator|=
name|transformer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"XSLT["
operator|+
name|transformer
operator|+
literal|"]"
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
specifier|synchronized
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Transformer
name|transformer
init|=
name|getTransformer
argument_list|()
decl_stmt|;
if|if
condition|(
name|transformer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No transformer configured!"
argument_list|)
throw|;
block|}
name|configureTransformer
argument_list|(
name|transformer
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
name|getSource
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|ResultHandler
name|resultHandler
init|=
name|resultHandlerFactory
operator|.
name|createResult
argument_list|()
decl_stmt|;
name|Result
name|result
init|=
name|resultHandler
operator|.
name|getResult
argument_list|()
decl_stmt|;
comment|// lets copy the headers before we invoke the transform in case they modify them
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|resultHandler
operator|.
name|setBody
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
comment|// Builder methods
comment|// -------------------------------------------------------------------------
comment|/**      * Creates an XSLT processor using the given transformer instance      */
DECL|method|xslt (Transformer transformer)
specifier|public
specifier|static
name|XsltBuilder
name|xslt
parameter_list|(
name|Transformer
name|transformer
parameter_list|)
block|{
return|return
operator|new
name|XsltBuilder
argument_list|(
name|transformer
argument_list|)
return|;
block|}
comment|/**      * Creates an XSLT processor using the given XSLT source      */
DECL|method|xslt (Source xslt)
specifier|public
specifier|static
name|XsltBuilder
name|xslt
parameter_list|(
name|Source
name|xslt
parameter_list|)
throws|throws
name|TransformerConfigurationException
block|{
name|notNull
argument_list|(
name|xslt
argument_list|,
literal|"xslt"
argument_list|)
expr_stmt|;
name|XsltBuilder
name|answer
init|=
operator|new
name|XsltBuilder
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setTransformerSource
argument_list|(
name|xslt
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates an XSLT processor using the given XSLT source      */
DECL|method|xslt (File xslt)
specifier|public
specifier|static
name|XsltBuilder
name|xslt
parameter_list|(
name|File
name|xslt
parameter_list|)
throws|throws
name|TransformerConfigurationException
block|{
name|notNull
argument_list|(
name|xslt
argument_list|,
literal|"xslt"
argument_list|)
expr_stmt|;
return|return
name|xslt
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|xslt
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates an XSLT processor using the given XSLT source      */
DECL|method|xslt (URL xslt)
specifier|public
specifier|static
name|XsltBuilder
name|xslt
parameter_list|(
name|URL
name|xslt
parameter_list|)
throws|throws
name|TransformerConfigurationException
throws|,
name|IOException
block|{
name|notNull
argument_list|(
name|xslt
argument_list|,
literal|"xslt"
argument_list|)
expr_stmt|;
return|return
name|xslt
argument_list|(
name|xslt
operator|.
name|openStream
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Creates an XSLT processor using the given XSLT source      */
DECL|method|xslt (InputStream xslt)
specifier|public
specifier|static
name|XsltBuilder
name|xslt
parameter_list|(
name|InputStream
name|xslt
parameter_list|)
throws|throws
name|TransformerConfigurationException
throws|,
name|IOException
block|{
name|notNull
argument_list|(
name|xslt
argument_list|,
literal|"xslt"
argument_list|)
expr_stmt|;
return|return
name|xslt
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|xslt
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sets the output as being a byte[]      */
DECL|method|outputBytes ()
specifier|public
name|XsltBuilder
name|outputBytes
parameter_list|()
block|{
name|setResultHandlerFactory
argument_list|(
operator|new
name|StreamResultHandlerFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the output as being a String      */
DECL|method|outputString ()
specifier|public
name|XsltBuilder
name|outputString
parameter_list|()
block|{
name|setResultHandlerFactory
argument_list|(
operator|new
name|StringResultHandlerFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the output as being a DOM      */
DECL|method|outputDOM ()
specifier|public
name|XsltBuilder
name|outputDOM
parameter_list|()
block|{
name|setResultHandlerFactory
argument_list|(
operator|new
name|DomResultHandlerFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|parameter (String name, Object value)
specifier|public
name|XsltBuilder
name|parameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|parameters
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
DECL|method|setParameters (Map<String, Object> parameters)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
DECL|method|getTransformer ()
specifier|public
name|Transformer
name|getTransformer
parameter_list|()
block|{
return|return
name|transformer
return|;
block|}
DECL|method|setTransformer (Transformer transformer)
specifier|public
name|void
name|setTransformer
parameter_list|(
name|Transformer
name|transformer
parameter_list|)
block|{
name|this
operator|.
name|transformer
operator|=
name|transformer
expr_stmt|;
block|}
DECL|method|isFailOnNullBody ()
specifier|public
name|boolean
name|isFailOnNullBody
parameter_list|()
block|{
return|return
name|failOnNullBody
return|;
block|}
DECL|method|setFailOnNullBody (boolean failOnNullBody)
specifier|public
name|void
name|setFailOnNullBody
parameter_list|(
name|boolean
name|failOnNullBody
parameter_list|)
block|{
name|this
operator|.
name|failOnNullBody
operator|=
name|failOnNullBody
expr_stmt|;
block|}
DECL|method|getResultHandlerFactory ()
specifier|public
name|ResultHandlerFactory
name|getResultHandlerFactory
parameter_list|()
block|{
return|return
name|resultHandlerFactory
return|;
block|}
DECL|method|setResultHandlerFactory (ResultHandlerFactory resultHandlerFactory)
specifier|public
name|void
name|setResultHandlerFactory
parameter_list|(
name|ResultHandlerFactory
name|resultHandlerFactory
parameter_list|)
block|{
name|this
operator|.
name|resultHandlerFactory
operator|=
name|resultHandlerFactory
expr_stmt|;
block|}
DECL|method|setTransformerSource (Source source)
specifier|public
name|void
name|setTransformerSource
parameter_list|(
name|Source
name|source
parameter_list|)
throws|throws
name|TransformerConfigurationException
block|{
name|setTransformer
argument_list|(
name|converter
operator|.
name|getTransformerFactory
argument_list|()
operator|.
name|newTransformer
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * Converts the inbound body to a {@link Source}      */
DECL|method|getSource (Exchange exchange)
specifier|protected
name|Source
name|getSource
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Source
name|source
init|=
name|in
operator|.
name|getBody
argument_list|(
name|Source
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isFailOnNullBody
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ExpectedBodyTypeException
argument_list|(
name|exchange
argument_list|,
name|Source
operator|.
name|class
argument_list|)
throw|;
block|}
else|else
block|{
try|try
block|{
name|source
operator|=
name|converter
operator|.
name|toSource
argument_list|(
name|converter
operator|.
name|createDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeTransformException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|source
return|;
block|}
comment|/**      * Configures the transformerwith exchange specific parameters      */
DECL|method|configureTransformer (Transformer transformer, Exchange exchange)
specifier|protected
name|void
name|configureTransformer
parameter_list|(
name|Transformer
name|transformer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|transformer
operator|.
name|clearParameters
argument_list|()
expr_stmt|;
name|addParameters
argument_list|(
name|transformer
argument_list|,
name|exchange
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|addParameters
argument_list|(
name|transformer
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|addParameters
argument_list|(
name|transformer
argument_list|,
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setParameter
argument_list|(
literal|"exchange"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setParameter
argument_list|(
literal|"in"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setParameter
argument_list|(
literal|"out"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|addParameters (Transformer transformer, Map<String, Object> map)
specifier|protected
name|void
name|addParameters
parameter_list|(
name|Transformer
name|transformer
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|propertyEntries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|propertyEntries
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|transformer
operator|.
name|setParameter
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

