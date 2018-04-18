begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xquery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xquery
package|;
end_package

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
name|Properties
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|lib
operator|.
name|ModuleURIResolver
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|query
operator|.
name|StaticQueryContext
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
name|impl
operator|.
name|ProcessorEndpoint
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
name|util
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
name|ServiceHelper
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
comment|/**  * Transforms the message using a XQuery template using Saxon.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.0.0"
argument_list|,
name|scheme
operator|=
literal|"xquery"
argument_list|,
name|title
operator|=
literal|"XQuery"
argument_list|,
name|syntax
operator|=
literal|"xquery:resourceUri"
argument_list|,
name|label
operator|=
literal|"transformation"
argument_list|)
DECL|class|XQueryEndpoint
specifier|public
class|class
name|XQueryEndpoint
extends|extends
name|ProcessorEndpoint
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
name|XQueryEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|xquery
specifier|private
specifier|volatile
name|XQueryBuilder
name|xquery
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configurationProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|configurationProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|staticQueryContext
specifier|private
name|StaticQueryContext
name|staticQueryContext
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
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
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
DECL|field|namespacePrefixes
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespacePrefixes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"DOM"
argument_list|)
DECL|field|resultsFormat
specifier|private
name|ResultFormat
name|resultsFormat
init|=
name|ResultFormat
operator|.
name|DOM
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|properties
specifier|private
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
DECL|field|resultType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|stripsAllWhiteSpace
specifier|private
name|boolean
name|stripsAllWhiteSpace
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|moduleURIResolver
specifier|private
name|ModuleURIResolver
name|moduleURIResolver
decl_stmt|;
annotation|@
name|UriParam
DECL|field|allowStAX
specifier|private
name|boolean
name|allowStAX
decl_stmt|;
annotation|@
name|UriParam
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
DECL|method|XQueryEndpoint (String endpointUri, Component component)
specifier|public
name|XQueryEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * The name of the template to load from classpath or file system      */
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
DECL|method|getConfiguration ()
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * To use a custom Saxon configuration      */
DECL|method|setConfiguration (Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getConfigurationProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getConfigurationProperties
parameter_list|()
block|{
return|return
name|configurationProperties
return|;
block|}
comment|/**      * To set custom Saxon configuration properties      */
DECL|method|setConfigurationProperties (Map<String, Object> configurationProperties)
specifier|public
name|void
name|setConfigurationProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|configurationProperties
parameter_list|)
block|{
name|this
operator|.
name|configurationProperties
operator|=
name|configurationProperties
expr_stmt|;
block|}
DECL|method|getStaticQueryContext ()
specifier|public
name|StaticQueryContext
name|getStaticQueryContext
parameter_list|()
block|{
return|return
name|staticQueryContext
return|;
block|}
comment|/**      * To use a custom Saxon StaticQueryContext      */
DECL|method|setStaticQueryContext (StaticQueryContext staticQueryContext)
specifier|public
name|void
name|setStaticQueryContext
parameter_list|(
name|StaticQueryContext
name|staticQueryContext
parameter_list|)
block|{
name|this
operator|.
name|staticQueryContext
operator|=
name|staticQueryContext
expr_stmt|;
block|}
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
comment|/**      * Additional parameters      */
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
DECL|method|getNamespacePrefixes ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespacePrefixes
parameter_list|()
block|{
return|return
name|namespacePrefixes
return|;
block|}
comment|/**      * Allows to control which namespace prefixes to use for a set of namespace mappings      */
DECL|method|setNamespacePrefixes (Map<String, String> namespacePrefixes)
specifier|public
name|void
name|setNamespacePrefixes
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespacePrefixes
parameter_list|)
block|{
name|this
operator|.
name|namespacePrefixes
operator|=
name|namespacePrefixes
expr_stmt|;
block|}
DECL|method|getResultsFormat ()
specifier|public
name|ResultFormat
name|getResultsFormat
parameter_list|()
block|{
return|return
name|resultsFormat
return|;
block|}
comment|/**      * What output result to use      */
DECL|method|setResultsFormat (ResultFormat resultsFormat)
specifier|public
name|void
name|setResultsFormat
parameter_list|(
name|ResultFormat
name|resultsFormat
parameter_list|)
block|{
name|this
operator|.
name|resultsFormat
operator|=
name|resultsFormat
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/**      * Properties to configure the serialization parameters      */
DECL|method|setProperties (Properties properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|getResultType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResultType
parameter_list|()
block|{
return|return
name|resultType
return|;
block|}
comment|/**      * What output result to use defined as a class      */
DECL|method|setResultType (Class<?> resultType)
specifier|public
name|void
name|setResultType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|this
operator|.
name|resultType
operator|=
name|resultType
expr_stmt|;
block|}
DECL|method|isStripsAllWhiteSpace ()
specifier|public
name|boolean
name|isStripsAllWhiteSpace
parameter_list|()
block|{
return|return
name|stripsAllWhiteSpace
return|;
block|}
comment|/**      * Whether to strip all whitespaces      */
DECL|method|setStripsAllWhiteSpace (boolean stripsAllWhiteSpace)
specifier|public
name|void
name|setStripsAllWhiteSpace
parameter_list|(
name|boolean
name|stripsAllWhiteSpace
parameter_list|)
block|{
name|this
operator|.
name|stripsAllWhiteSpace
operator|=
name|stripsAllWhiteSpace
expr_stmt|;
block|}
DECL|method|getModuleURIResolver ()
specifier|public
name|ModuleURIResolver
name|getModuleURIResolver
parameter_list|()
block|{
return|return
name|moduleURIResolver
return|;
block|}
comment|/**      * To use the custom {@link ModuleURIResolver}      */
DECL|method|setModuleURIResolver (ModuleURIResolver moduleURIResolver)
specifier|public
name|void
name|setModuleURIResolver
parameter_list|(
name|ModuleURIResolver
name|moduleURIResolver
parameter_list|)
block|{
name|this
operator|.
name|moduleURIResolver
operator|=
name|moduleURIResolver
expr_stmt|;
block|}
DECL|method|isAllowStAX ()
specifier|public
name|boolean
name|isAllowStAX
parameter_list|()
block|{
return|return
name|allowStAX
return|;
block|}
comment|/**      * Whether to allow using StAX mode      */
DECL|method|setAllowStAX (boolean allowStAX)
specifier|public
name|void
name|setAllowStAX
parameter_list|(
name|boolean
name|allowStAX
parameter_list|)
block|{
name|this
operator|.
name|allowStAX
operator|=
name|allowStAX
expr_stmt|;
block|}
DECL|method|getHeaderName ()
specifier|public
name|String
name|getHeaderName
parameter_list|()
block|{
return|return
name|headerName
return|;
block|}
comment|/**      * To use a Camel Message header as the input source instead of Message body.      */
DECL|method|setHeaderName (String headerName)
specifier|public
name|void
name|setHeaderName
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|this
operator|.
name|headerName
operator|=
name|headerName
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} using schema resource: {}"
argument_list|,
name|this
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsUrl
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
decl_stmt|;
name|this
operator|.
name|xquery
operator|=
name|XQueryBuilder
operator|.
name|xquery
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setConfiguration
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setConfigurationProperties
argument_list|(
name|getConfigurationProperties
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setStaticQueryContext
argument_list|(
name|getStaticQueryContext
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setParameters
argument_list|(
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setNamespaces
argument_list|(
name|namespacePrefixes
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setResultsFormat
argument_list|(
name|getResultsFormat
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setProperties
argument_list|(
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setResultType
argument_list|(
name|getResultType
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setStripsAllWhiteSpace
argument_list|(
name|isStripsAllWhiteSpace
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setAllowStAX
argument_list|(
name|isAllowStAX
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setHeaderName
argument_list|(
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xquery
operator|.
name|setModuleURIResolver
argument_list|(
name|getModuleURIResolver
argument_list|()
argument_list|)
expr_stmt|;
name|setProcessor
argument_list|(
name|xquery
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|xquery
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|xquery
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

