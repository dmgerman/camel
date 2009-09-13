begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|cache
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|TransformerFactory
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
name|dom
operator|.
name|DOMResult
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
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Ehcache
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
name|component
operator|.
name|cache
operator|.
name|factory
operator|.
name|CacheManagerFactory
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
name|IOConverter
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
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
DECL|class|CacheBasedXPathReplacer
specifier|public
class|class
name|CacheBasedXPathReplacer
extends|extends
name|CacheValidate
implements|implements
name|Processor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CacheBasedXPathReplacer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cacheName
specifier|private
name|String
name|cacheName
decl_stmt|;
DECL|field|key
specifier|private
name|String
name|key
decl_stmt|;
DECL|field|xpath
specifier|private
name|String
name|xpath
decl_stmt|;
DECL|field|cacheManager
specifier|private
name|CacheManager
name|cacheManager
decl_stmt|;
DECL|field|cache
specifier|private
name|Ehcache
name|cache
decl_stmt|;
DECL|field|document
specifier|private
name|Document
name|document
decl_stmt|;
DECL|field|source
specifier|private
name|DOMSource
name|source
decl_stmt|;
DECL|field|result
specifier|private
name|DOMResult
name|result
decl_stmt|;
DECL|method|CacheBasedXPathReplacer (String cacheName, String key, String xpath)
specifier|public
name|CacheBasedXPathReplacer
parameter_list|(
name|String
name|cacheName
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|xpath
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
if|if
condition|(
name|cacheName
operator|.
name|contains
argument_list|(
literal|"cache://"
argument_list|)
condition|)
block|{
name|this
operator|.
name|setCacheName
argument_list|(
name|cacheName
operator|.
name|replace
argument_list|(
literal|"cache://"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|setCacheName
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|xpath
operator|=
name|xpath
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Cache the buffer to the specified Cache against the specified key
name|cacheManager
operator|=
operator|new
name|CacheManagerFactory
argument_list|()
operator|.
name|instantiateCacheManager
argument_list|()
expr_stmt|;
if|if
condition|(
name|isValid
argument_list|(
name|cacheManager
argument_list|,
name|cacheName
argument_list|,
name|key
argument_list|)
condition|)
block|{
name|cache
operator|=
name|cacheManager
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Replacing XPath value "
operator|+
name|xpath
operator|+
literal|"in Message with value stored against key "
operator|+
name|key
operator|+
literal|" in CacheName "
operator|+
name|cacheName
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CACHE_KEY"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
try|try
block|{
name|document
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|InputStream
name|cis
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getObjectValue
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Document
name|cacheValueDocument
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|cis
argument_list|)
decl_stmt|;
comment|// Create/setup the Transformer
name|XmlConverter
name|xmlConverter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|String
name|xslString
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
operator|new
name|File
argument_list|(
literal|"./src/main/resources/xpathreplacer.xsl"
argument_list|)
argument_list|)
decl_stmt|;
name|xslString
operator|=
name|xslString
operator|.
name|replace
argument_list|(
literal|"##match_token##"
argument_list|,
name|xpath
argument_list|)
expr_stmt|;
name|Source
name|xslSource
init|=
name|xmlConverter
operator|.
name|toStreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xslString
argument_list|)
argument_list|)
decl_stmt|;
name|TransformerFactory
name|transformerFactory
init|=
name|xmlConverter
operator|.
name|createTransformerFactory
argument_list|()
decl_stmt|;
name|Transformer
name|transformer
init|=
name|transformerFactory
operator|.
name|newTransformer
argument_list|(
name|xslSource
argument_list|)
decl_stmt|;
name|source
operator|=
name|xmlConverter
operator|.
name|toSource
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|result
operator|=
operator|new
name|DOMResult
argument_list|()
expr_stmt|;
name|transformer
operator|.
name|setParameter
argument_list|(
literal|"cacheValue"
argument_list|,
name|cacheValueDocument
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
block|}
finally|finally
block|{
name|cis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|IOConverter
operator|.
name|toInputStrean
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|result
operator|.
name|getNode
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|cacheName
return|;
block|}
DECL|method|setCacheName (String cacheName)
specifier|public
name|void
name|setCacheName
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|setKey (String key)
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
DECL|method|getXpath ()
specifier|public
name|String
name|getXpath
parameter_list|()
block|{
return|return
name|xpath
return|;
block|}
DECL|method|setXpath (String xpath)
specifier|public
name|void
name|setXpath
parameter_list|(
name|String
name|xpath
parameter_list|)
block|{
name|this
operator|.
name|xpath
operator|=
name|xpath
expr_stmt|;
block|}
block|}
end_class

end_unit

