begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yql.configuration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yql
operator|.
name|configuration
package|;
end_package

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
name|UriParams
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

begin_comment
comment|/**  * YQL configuration that should reflect https://developer.yahoo.com/yql/guide/users-overview.html  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|YqlConfiguration
specifier|public
class|class
name|YqlConfiguration
block|{
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The YQL statement to execute."
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|enums
operator|=
literal|"json,xml"
argument_list|,
name|defaultValue
operator|=
literal|"json"
argument_list|,
name|description
operator|=
literal|"The expected format. Allowed values: xml or json."
argument_list|)
DECL|field|format
specifier|private
name|String
name|format
init|=
literal|"json"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The name of the JavaScript callback function for JSONP format. If callback is set and if format=json, then the response format is JSON. For more "
operator|+
literal|"information on using XML instead of JSON, see JSONP-X."
argument_list|)
DECL|field|callback
specifier|private
name|String
name|callback
init|=
literal|""
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"When given the value optimized, the projected fields in SELECT statements that may be returned in separate item elements in the response are "
operator|+
literal|"optimized to be in a single item element instead. The only allowed value is optimized."
argument_list|)
DECL|field|crossProduct
specifier|private
name|String
name|crossProduct
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|,
name|description
operator|=
literal|"If true, diagnostic information is returned with the response."
argument_list|)
DECL|field|diagnostics
specifier|private
name|boolean
name|diagnostics
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|,
name|description
operator|=
literal|"If true, and if diagnostic is set to true, debug data is returned with the response."
argument_list|)
DECL|field|debug
specifier|private
name|boolean
name|debug
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Allows you to use multiple Open Data Tables through a YQL environment file."
argument_list|)
DECL|field|env
specifier|private
name|String
name|env
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Enables lossless JSON processing. The only allowed value is new."
argument_list|)
DECL|field|jsonCompat
specifier|private
name|String
name|jsonCompat
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Option to disable throwing the YqlHttpException in case of failed responses from the remote server. "
operator|+
literal|"This allows you to get all responses regardless of the HTTP status code."
argument_list|)
DECL|field|throwExceptionOnFailure
specifier|private
name|boolean
name|throwExceptionOnFailure
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Option to use HTTPS to communicate with YQL."
argument_list|)
DECL|field|https
specifier|private
name|boolean
name|https
init|=
literal|true
decl_stmt|;
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
comment|/**      * The YQL statement to execute.      */
DECL|method|setQuery (final String query)
specifier|public
name|void
name|setQuery
parameter_list|(
specifier|final
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|getFormat ()
specifier|public
name|String
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
comment|/**      * The expected format. Allowed values: xml or json.      */
DECL|method|setFormat (final String format)
specifier|public
name|void
name|setFormat
parameter_list|(
specifier|final
name|String
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
DECL|method|getCallback ()
specifier|public
name|String
name|getCallback
parameter_list|()
block|{
return|return
name|callback
return|;
block|}
comment|/**      * The name of the JavaScript callback function for JSONP format. If callback is set and if format=json, then the response format is JSON. For more      * information on using XML instead of JSON, see JSONP-X. https://developer.yahoo.com/yql/guide/response.html      */
DECL|method|setCallback (final String callback)
specifier|public
name|void
name|setCallback
parameter_list|(
specifier|final
name|String
name|callback
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
DECL|method|getCrossProduct ()
specifier|public
name|String
name|getCrossProduct
parameter_list|()
block|{
return|return
name|crossProduct
return|;
block|}
comment|/**      * When given the value optimized, the projected fields in SELECT statements that may be returned in separate item elements in the response are optimized to be in a single item element instead.      * The only allowed value is optimized. More information https://developer.yahoo.com/yql/guide/response.html#response-optimizing=      */
DECL|method|setCrossProduct (final String crossProduct)
specifier|public
name|void
name|setCrossProduct
parameter_list|(
specifier|final
name|String
name|crossProduct
parameter_list|)
block|{
name|this
operator|.
name|crossProduct
operator|=
name|crossProduct
expr_stmt|;
block|}
DECL|method|isDiagnostics ()
specifier|public
name|boolean
name|isDiagnostics
parameter_list|()
block|{
return|return
name|diagnostics
return|;
block|}
comment|/**      * If true, diagnostic information is returned with the response.      */
DECL|method|setDiagnostics (final boolean diagnostics)
specifier|public
name|void
name|setDiagnostics
parameter_list|(
specifier|final
name|boolean
name|diagnostics
parameter_list|)
block|{
name|this
operator|.
name|diagnostics
operator|=
name|diagnostics
expr_stmt|;
block|}
DECL|method|isDebug ()
specifier|public
name|boolean
name|isDebug
parameter_list|()
block|{
return|return
name|debug
return|;
block|}
comment|/**      * If true, and if diagnostic is set to true, debug data is returned with the response.      * More information: https://developer.yahoo.com/yql/guide/dev-external_tables.html#odt-enable-logging=      */
DECL|method|setDebug (final boolean debug)
specifier|public
name|void
name|setDebug
parameter_list|(
specifier|final
name|boolean
name|debug
parameter_list|)
block|{
name|this
operator|.
name|debug
operator|=
name|debug
expr_stmt|;
block|}
DECL|method|getEnv ()
specifier|public
name|String
name|getEnv
parameter_list|()
block|{
return|return
name|env
return|;
block|}
comment|/**      * Allows you to use multiple Open Data Tables through a YQL environment file.      * More information https://developer.yahoo.com/yql/guide/yql_storage.html#using-records-env-files=      */
DECL|method|setEnv (final String env)
specifier|public
name|void
name|setEnv
parameter_list|(
specifier|final
name|String
name|env
parameter_list|)
block|{
name|this
operator|.
name|env
operator|=
name|env
expr_stmt|;
block|}
DECL|method|getJsonCompat ()
specifier|public
name|String
name|getJsonCompat
parameter_list|()
block|{
return|return
name|jsonCompat
return|;
block|}
comment|/**      * Enables lossless JSON processing. The only allowed value is new.      * More information https://developer.yahoo.com/yql/guide/response.html#json-to-json=      */
DECL|method|setJsonCompat (final String jsonCompat)
specifier|public
name|void
name|setJsonCompat
parameter_list|(
specifier|final
name|String
name|jsonCompat
parameter_list|)
block|{
name|this
operator|.
name|jsonCompat
operator|=
name|jsonCompat
expr_stmt|;
block|}
DECL|method|isThrowExceptionOnFailure ()
specifier|public
name|boolean
name|isThrowExceptionOnFailure
parameter_list|()
block|{
return|return
name|throwExceptionOnFailure
return|;
block|}
comment|/**      * Option to disable throwing the YqlHttpException in case of failed responses from the remote server.      * This allows you to get all responses regardless of the HTTP status code.      */
DECL|method|setThrowExceptionOnFailure (final boolean throwExceptionOnFailure)
specifier|public
name|void
name|setThrowExceptionOnFailure
parameter_list|(
specifier|final
name|boolean
name|throwExceptionOnFailure
parameter_list|)
block|{
name|this
operator|.
name|throwExceptionOnFailure
operator|=
name|throwExceptionOnFailure
expr_stmt|;
block|}
DECL|method|isHttps ()
specifier|public
name|boolean
name|isHttps
parameter_list|()
block|{
return|return
name|https
return|;
block|}
comment|/**      * Option to use HTTPS to communicate with YQL.      */
DECL|method|setHttps (final boolean https)
specifier|public
name|void
name|setHttps
parameter_list|(
specifier|final
name|boolean
name|https
parameter_list|)
block|{
name|this
operator|.
name|https
operator|=
name|https
expr_stmt|;
block|}
block|}
end_class

end_unit

