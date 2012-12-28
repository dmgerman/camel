begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.urlrewrite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|urlrewrite
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|CamelContext
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
name|CamelContextAware
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
name|IsSingleton
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
name|ServiceSupport
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

begin_import
import|import
name|org
operator|.
name|tuckey
operator|.
name|web
operator|.
name|filters
operator|.
name|urlrewrite
operator|.
name|Conf
import|;
end_import

begin_import
import|import
name|org
operator|.
name|tuckey
operator|.
name|web
operator|.
name|filters
operator|.
name|urlrewrite
operator|.
name|RewrittenUrl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|tuckey
operator|.
name|web
operator|.
name|filters
operator|.
name|urlrewrite
operator|.
name|UrlRewriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|tuckey
operator|.
name|web
operator|.
name|filters
operator|.
name|urlrewrite
operator|.
name|utils
operator|.
name|ModRewriteConfLoader
import|;
end_import

begin_comment
comment|/**  * Url rewrite filter based on<a href="https://code.google.com/p/urlrewritefilter/">url rewrite filter</a>  */
end_comment

begin_class
DECL|class|UrlRewriteFilter
specifier|public
specifier|abstract
class|class
name|UrlRewriteFilter
extends|extends
name|ServiceSupport
implements|implements
name|CamelContextAware
implements|,
name|IsSingleton
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|UrlRewriteFilter
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// TODO: Add support in camel-http4 and camel-ahc
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|conf
specifier|protected
name|Conf
name|conf
decl_stmt|;
DECL|field|urlRewriter
specifier|protected
name|UrlRewriter
name|urlRewriter
decl_stmt|;
DECL|field|configFile
specifier|protected
name|String
name|configFile
decl_stmt|;
DECL|field|modRewriteConfFile
specifier|protected
name|String
name|modRewriteConfFile
decl_stmt|;
DECL|field|modRewriteConfText
specifier|protected
name|String
name|modRewriteConfText
decl_stmt|;
DECL|field|useQueryString
specifier|protected
name|boolean
name|useQueryString
decl_stmt|;
DECL|field|useContext
specifier|protected
name|boolean
name|useContext
decl_stmt|;
DECL|field|defaultMatchType
specifier|protected
name|String
name|defaultMatchType
decl_stmt|;
DECL|field|decodeUsing
specifier|protected
name|String
name|decodeUsing
decl_stmt|;
DECL|method|rewrite (String url, HttpServletRequest request)
specifier|public
name|String
name|rewrite
parameter_list|(
name|String
name|url
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|)
throws|throws
name|Exception
block|{
name|RewrittenUrl
name|response
init|=
name|urlRewriter
operator|.
name|processRequest
argument_list|(
name|request
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|String
name|answer
init|=
name|response
operator|.
name|getTarget
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Rewrite url: {} -> {}"
argument_list|,
name|url
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Rewrite using original url: {}"
argument_list|,
name|url
argument_list|)
expr_stmt|;
return|return
name|url
return|;
block|}
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getConf ()
specifier|public
name|Conf
name|getConf
parameter_list|()
block|{
return|return
name|conf
return|;
block|}
DECL|method|setConf (Conf conf)
specifier|public
name|void
name|setConf
parameter_list|(
name|Conf
name|conf
parameter_list|)
block|{
name|this
operator|.
name|conf
operator|=
name|conf
expr_stmt|;
block|}
DECL|method|getUrlRewriter ()
specifier|public
name|UrlRewriter
name|getUrlRewriter
parameter_list|()
block|{
return|return
name|urlRewriter
return|;
block|}
DECL|method|setUrlRewriter (UrlRewriter urlRewriter)
specifier|public
name|void
name|setUrlRewriter
parameter_list|(
name|UrlRewriter
name|urlRewriter
parameter_list|)
block|{
name|this
operator|.
name|urlRewriter
operator|=
name|urlRewriter
expr_stmt|;
block|}
DECL|method|getConfigFile ()
specifier|public
name|String
name|getConfigFile
parameter_list|()
block|{
return|return
name|configFile
return|;
block|}
DECL|method|setConfigFile (String configFile)
specifier|public
name|void
name|setConfigFile
parameter_list|(
name|String
name|configFile
parameter_list|)
block|{
name|this
operator|.
name|configFile
operator|=
name|configFile
expr_stmt|;
block|}
DECL|method|getModRewriteConfText ()
specifier|public
name|String
name|getModRewriteConfText
parameter_list|()
block|{
return|return
name|modRewriteConfText
return|;
block|}
DECL|method|setModRewriteConfText (String modRewriteConfText)
specifier|public
name|void
name|setModRewriteConfText
parameter_list|(
name|String
name|modRewriteConfText
parameter_list|)
block|{
name|this
operator|.
name|modRewriteConfText
operator|=
name|modRewriteConfText
expr_stmt|;
block|}
DECL|method|getModRewriteConfFile ()
specifier|public
name|String
name|getModRewriteConfFile
parameter_list|()
block|{
return|return
name|modRewriteConfFile
return|;
block|}
DECL|method|setModRewriteConfFile (String modRewriteConfFile)
specifier|public
name|void
name|setModRewriteConfFile
parameter_list|(
name|String
name|modRewriteConfFile
parameter_list|)
block|{
name|this
operator|.
name|modRewriteConfFile
operator|=
name|modRewriteConfFile
expr_stmt|;
block|}
DECL|method|isUseQueryString ()
specifier|public
name|boolean
name|isUseQueryString
parameter_list|()
block|{
return|return
name|useQueryString
return|;
block|}
DECL|method|setUseQueryString (boolean useQueryString)
specifier|public
name|void
name|setUseQueryString
parameter_list|(
name|boolean
name|useQueryString
parameter_list|)
block|{
name|this
operator|.
name|useQueryString
operator|=
name|useQueryString
expr_stmt|;
block|}
DECL|method|isUseContext ()
specifier|public
name|boolean
name|isUseContext
parameter_list|()
block|{
return|return
name|useContext
return|;
block|}
DECL|method|setUseContext (boolean useContext)
specifier|public
name|void
name|setUseContext
parameter_list|(
name|boolean
name|useContext
parameter_list|)
block|{
name|this
operator|.
name|useContext
operator|=
name|useContext
expr_stmt|;
block|}
DECL|method|getDefaultMatchType ()
specifier|public
name|String
name|getDefaultMatchType
parameter_list|()
block|{
return|return
name|defaultMatchType
return|;
block|}
DECL|method|setDefaultMatchType (String defaultMatchType)
specifier|public
name|void
name|setDefaultMatchType
parameter_list|(
name|String
name|defaultMatchType
parameter_list|)
block|{
name|this
operator|.
name|defaultMatchType
operator|=
name|defaultMatchType
expr_stmt|;
block|}
DECL|method|getDecodeUsing ()
specifier|public
name|String
name|getDecodeUsing
parameter_list|()
block|{
return|return
name|decodeUsing
return|;
block|}
DECL|method|setDecodeUsing (String decodeUsing)
specifier|public
name|void
name|setDecodeUsing
parameter_list|(
name|String
name|decodeUsing
parameter_list|)
block|{
name|this
operator|.
name|decodeUsing
operator|=
name|decodeUsing
expr_stmt|;
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
if|if
condition|(
name|conf
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|modRewriteConfFile
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using mod rewrite config file: {} as config for urlRewrite"
argument_list|,
name|modRewriteConfFile
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|loadResourceAsStream
argument_list|(
name|modRewriteConfFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot load mod rewrite config file: "
operator|+
name|modRewriteConfFile
argument_list|)
throw|;
block|}
try|try
block|{
name|String
name|text
init|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|ModRewriteConfLoader
name|loader
init|=
operator|new
name|ModRewriteConfLoader
argument_list|()
decl_stmt|;
name|conf
operator|=
operator|new
name|Conf
argument_list|()
expr_stmt|;
name|loader
operator|.
name|process
argument_list|(
name|text
argument_list|,
name|conf
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|modRewriteConfText
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using modRewriteConfText: {} as config for urlRewrite"
argument_list|,
name|modRewriteConfText
argument_list|)
expr_stmt|;
name|ModRewriteConfLoader
name|loader
init|=
operator|new
name|ModRewriteConfLoader
argument_list|()
decl_stmt|;
name|conf
operator|=
operator|new
name|Conf
argument_list|()
expr_stmt|;
name|loader
operator|.
name|process
argument_list|(
name|modRewriteConfText
argument_list|,
name|conf
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configFile
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using config file: {} as config for urlRewrite"
argument_list|,
name|configFile
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|loadResourceAsStream
argument_list|(
name|configFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot load config file: "
operator|+
name|configFile
argument_list|)
throw|;
block|}
try|try
block|{
name|conf
operator|=
operator|new
name|Conf
argument_list|(
name|is
argument_list|,
name|configFile
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|conf
operator|!=
literal|null
condition|)
block|{
comment|// set options before initializing
name|conf
operator|.
name|setUseQueryString
argument_list|(
name|isUseQueryString
argument_list|()
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setUseContext
argument_list|(
name|isUseContext
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getDefaultMatchType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|conf
operator|.
name|setDefaultMatchType
argument_list|(
name|getDefaultMatchType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getDecodeUsing
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|conf
operator|.
name|setDecodeUsing
argument_list|(
name|getDecodeUsing
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|conf
operator|.
name|initialise
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|conf
operator|==
literal|null
operator|||
operator|!
name|conf
operator|.
name|isOk
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Error configuring config file: "
operator|+
name|configFile
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|urlRewriter
operator|==
literal|null
condition|)
block|{
name|urlRewriter
operator|=
operator|new
name|UrlRewriter
argument_list|(
name|conf
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
block|{     }
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Shutting down urlRewrite"
argument_list|)
expr_stmt|;
name|urlRewriter
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|conf
operator|=
literal|null
expr_stmt|;
name|urlRewriter
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
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
block|}
end_class

end_unit

