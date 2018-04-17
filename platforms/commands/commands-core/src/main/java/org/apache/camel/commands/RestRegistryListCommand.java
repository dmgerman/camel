begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * List the Camel REST services from the Rest registry available in the JVM.  */
end_comment

begin_class
DECL|class|RestRegistryListCommand
specifier|public
class|class
name|RestRegistryListCommand
extends|extends
name|AbstractContextCommand
block|{
DECL|field|URL_COLUMN_NAME
specifier|private
specifier|static
specifier|final
name|String
name|URL_COLUMN_NAME
init|=
literal|"Url"
decl_stmt|;
DECL|field|BASE_PATH_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|BASE_PATH_LABEL
init|=
literal|"Base Path"
decl_stmt|;
DECL|field|URI_TEMPLATE_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|URI_TEMPLATE_LABEL
init|=
literal|"Uri Template"
decl_stmt|;
DECL|field|METHOD_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|METHOD_COLUMN_LABEL
init|=
literal|"Method"
decl_stmt|;
DECL|field|STATE_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|STATE_COLUMN_LABEL
init|=
literal|"State"
decl_stmt|;
DECL|field|ROUTE_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|ROUTE_COLUMN_LABEL
init|=
literal|"Route Id"
decl_stmt|;
DECL|field|DEFAULT_COLUMN_WIDTH_INCREMENT
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_COLUMN_WIDTH_INCREMENT
init|=
literal|0
decl_stmt|;
DECL|field|DEFAULT_FIELD_PREAMBLE
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_FIELD_PREAMBLE
init|=
literal|" "
decl_stmt|;
DECL|field|DEFAULT_FIELD_POSTAMBLE
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_FIELD_POSTAMBLE
init|=
literal|" "
decl_stmt|;
DECL|field|DEFAULT_HEADER_PREAMBLE
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_HEADER_PREAMBLE
init|=
literal|" "
decl_stmt|;
DECL|field|DEFAULT_HEADER_POSTAMBLE
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_HEADER_POSTAMBLE
init|=
literal|" "
decl_stmt|;
DECL|field|DEFAULT_FORMAT_BUFFER_LENGTH
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_FORMAT_BUFFER_LENGTH
init|=
literal|24
decl_stmt|;
comment|// endpoint uris can be very long so clip by default after 120 chars
DECL|field|MAX_COLUMN_WIDTH
specifier|private
specifier|static
specifier|final
name|int
name|MAX_COLUMN_WIDTH
init|=
literal|120
decl_stmt|;
DECL|field|MIN_COLUMN_WIDTH
specifier|private
specifier|static
specifier|final
name|int
name|MIN_COLUMN_WIDTH
init|=
literal|12
decl_stmt|;
DECL|field|decode
name|boolean
name|decode
init|=
literal|true
decl_stmt|;
DECL|field|verbose
name|boolean
name|verbose
decl_stmt|;
DECL|method|RestRegistryListCommand (String context, boolean decode, boolean verbose)
specifier|public
name|RestRegistryListCommand
parameter_list|(
name|String
name|context
parameter_list|,
name|boolean
name|decode
parameter_list|,
name|boolean
name|verbose
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|decode
operator|=
name|decode
expr_stmt|;
name|this
operator|.
name|verbose
operator|=
name|verbose
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|performContextCommand (CamelController camelController, String contextName, PrintStream out, PrintStream err)
specifier|protected
name|Object
name|performContextCommand
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|String
name|contextName
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|services
init|=
name|camelController
operator|.
name|getRestServices
argument_list|(
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|services
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"There are no REST services"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|columnWidths
init|=
name|computeColumnWidths
argument_list|(
name|services
argument_list|)
decl_stmt|;
specifier|final
name|String
name|headerFormat
init|=
name|buildFormatString
argument_list|(
name|columnWidths
argument_list|,
literal|true
argument_list|,
name|verbose
argument_list|)
decl_stmt|;
specifier|final
name|String
name|rowFormat
init|=
name|buildFormatString
argument_list|(
name|columnWidths
argument_list|,
literal|false
argument_list|,
name|verbose
argument_list|)
decl_stmt|;
if|if
condition|(
name|services
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|verbose
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|headerFormat
argument_list|,
name|URL_COLUMN_NAME
argument_list|,
name|BASE_PATH_LABEL
argument_list|,
name|URI_TEMPLATE_LABEL
argument_list|,
name|METHOD_COLUMN_LABEL
argument_list|,
name|STATE_COLUMN_LABEL
argument_list|,
name|ROUTE_COLUMN_LABEL
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|headerFormat
argument_list|,
literal|"---"
argument_list|,
literal|"---------"
argument_list|,
literal|"------------"
argument_list|,
literal|"------"
argument_list|,
literal|"-----"
argument_list|,
literal|"--------"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|headerFormat
argument_list|,
name|BASE_PATH_LABEL
argument_list|,
name|URI_TEMPLATE_LABEL
argument_list|,
name|METHOD_COLUMN_LABEL
argument_list|,
name|STATE_COLUMN_LABEL
argument_list|,
name|ROUTE_COLUMN_LABEL
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|headerFormat
argument_list|,
literal|"---------"
argument_list|,
literal|"------------"
argument_list|,
literal|"------"
argument_list|,
literal|"-----"
argument_list|,
literal|"--------"
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|services
control|)
block|{
name|String
name|uri
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|verbose
condition|)
block|{
name|uri
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
expr_stmt|;
if|if
condition|(
name|decode
condition|)
block|{
comment|// decode uri so its more human readable
name|uri
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|uri
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
comment|// sanitize and mask uri so we dont see passwords
name|uri
operator|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
name|String
name|basePath
init|=
name|row
operator|.
name|get
argument_list|(
literal|"basePath"
argument_list|)
decl_stmt|;
name|String
name|uriTemplate
init|=
name|row
operator|.
name|get
argument_list|(
literal|"uriTemplate"
argument_list|)
operator|!=
literal|null
condition|?
name|row
operator|.
name|get
argument_list|(
literal|"uriTemplate"
argument_list|)
else|:
literal|""
decl_stmt|;
name|String
name|method
init|=
name|row
operator|.
name|get
argument_list|(
literal|"method"
argument_list|)
decl_stmt|;
name|String
name|state
init|=
name|row
operator|.
name|get
argument_list|(
literal|"state"
argument_list|)
decl_stmt|;
name|String
name|route
init|=
name|row
operator|.
name|get
argument_list|(
literal|"routeId"
argument_list|)
decl_stmt|;
if|if
condition|(
name|verbose
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|rowFormat
argument_list|,
name|uri
argument_list|,
name|basePath
argument_list|,
name|uriTemplate
argument_list|,
name|method
argument_list|,
name|state
argument_list|,
name|route
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|rowFormat
argument_list|,
name|basePath
argument_list|,
name|uriTemplate
argument_list|,
name|method
argument_list|,
name|state
argument_list|,
name|route
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|computeColumnWidths (List<Map<String, String>> services)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|computeColumnWidths
parameter_list|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|services
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|maxUriLen
init|=
literal|0
decl_stmt|;
name|int
name|maxBasePathLen
init|=
literal|0
decl_stmt|;
name|int
name|maxUriTemplateLen
init|=
literal|0
decl_stmt|;
name|int
name|maxMethodLen
init|=
literal|0
decl_stmt|;
name|int
name|maxStatusLen
init|=
literal|0
decl_stmt|;
name|int
name|maxRouteLen
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|services
control|)
block|{
name|String
name|uri
init|=
name|row
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|decode
condition|)
block|{
comment|// decode uri so its more human readable
name|uri
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|uri
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
comment|// sanitize and mask uri so we dont see passwords
name|uri
operator|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|maxUriLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxUriLen
argument_list|,
name|uri
operator|==
literal|null
condition|?
literal|0
else|:
name|uri
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|basePath
init|=
name|row
operator|.
name|get
argument_list|(
literal|"basePath"
argument_list|)
decl_stmt|;
name|maxBasePathLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxBasePathLen
argument_list|,
name|basePath
operator|==
literal|null
condition|?
literal|0
else|:
name|basePath
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|uriTemplate
init|=
name|row
operator|.
name|get
argument_list|(
literal|"uriTemplate"
argument_list|)
decl_stmt|;
name|maxUriTemplateLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxUriTemplateLen
argument_list|,
name|uriTemplate
operator|==
literal|null
condition|?
literal|0
else|:
name|uriTemplate
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|method
init|=
name|row
operator|.
name|get
argument_list|(
literal|"method"
argument_list|)
decl_stmt|;
name|maxMethodLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxMethodLen
argument_list|,
name|method
operator|==
literal|null
condition|?
literal|0
else|:
name|method
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|row
operator|.
name|get
argument_list|(
literal|"state"
argument_list|)
decl_stmt|;
name|maxStatusLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxStatusLen
argument_list|,
name|status
operator|==
literal|null
condition|?
literal|0
else|:
name|status
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|routeId
init|=
name|row
operator|.
name|get
argument_list|(
literal|"routeId"
argument_list|)
decl_stmt|;
name|maxRouteLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxRouteLen
argument_list|,
name|routeId
operator|==
literal|null
condition|?
literal|0
else|:
name|routeId
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|retval
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|URL_COLUMN_NAME
argument_list|,
name|maxUriLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|BASE_PATH_LABEL
argument_list|,
name|maxBasePathLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|URI_TEMPLATE_LABEL
argument_list|,
name|maxUriTemplateLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|METHOD_COLUMN_LABEL
argument_list|,
name|maxMethodLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|STATE_COLUMN_LABEL
argument_list|,
name|maxStatusLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|ROUTE_COLUMN_LABEL
argument_list|,
name|maxRouteLen
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
DECL|method|buildFormatString (final Map<String, Integer> columnWidths, final boolean isHeader, final boolean isVerbose)
specifier|private
name|String
name|buildFormatString
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|columnWidths
parameter_list|,
specifier|final
name|boolean
name|isHeader
parameter_list|,
specifier|final
name|boolean
name|isVerbose
parameter_list|)
block|{
specifier|final
name|String
name|fieldPreamble
decl_stmt|;
specifier|final
name|String
name|fieldPostamble
decl_stmt|;
specifier|final
name|int
name|columnWidthIncrement
decl_stmt|;
if|if
condition|(
name|isHeader
condition|)
block|{
name|fieldPreamble
operator|=
name|DEFAULT_HEADER_PREAMBLE
expr_stmt|;
name|fieldPostamble
operator|=
name|DEFAULT_HEADER_POSTAMBLE
expr_stmt|;
block|}
else|else
block|{
name|fieldPreamble
operator|=
name|DEFAULT_FIELD_PREAMBLE
expr_stmt|;
name|fieldPostamble
operator|=
name|DEFAULT_FIELD_POSTAMBLE
expr_stmt|;
block|}
name|columnWidthIncrement
operator|=
name|DEFAULT_COLUMN_WIDTH_INCREMENT
expr_stmt|;
name|int
name|uriLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|URL_COLUMN_NAME
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|basePathLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|BASE_PATH_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|uriTemplateLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|URI_TEMPLATE_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|methodLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|METHOD_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|statusLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|STATE_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|routeLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|ROUTE_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
decl_stmt|;
name|uriLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|uriLen
argument_list|)
expr_stmt|;
name|basePathLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|basePathLen
argument_list|)
expr_stmt|;
name|uriTemplateLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|uriTemplateLen
argument_list|)
expr_stmt|;
name|methodLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|methodLen
argument_list|)
expr_stmt|;
name|statusLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|statusLen
argument_list|)
expr_stmt|;
comment|// last row does not have min width
specifier|final
name|StringBuilder
name|retval
init|=
operator|new
name|StringBuilder
argument_list|(
name|DEFAULT_FORMAT_BUFFER_LENGTH
argument_list|)
decl_stmt|;
if|if
condition|(
name|isVerbose
condition|)
block|{
name|retval
operator|.
name|append
argument_list|(
name|fieldPreamble
argument_list|)
operator|.
name|append
argument_list|(
literal|"%-"
argument_list|)
operator|.
name|append
argument_list|(
name|uriLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|uriLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'s'
argument_list|)
operator|.
name|append
argument_list|(
name|fieldPostamble
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|retval
operator|.
name|append
argument_list|(
name|fieldPreamble
argument_list|)
operator|.
name|append
argument_list|(
literal|"%-"
argument_list|)
operator|.
name|append
argument_list|(
name|basePathLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|basePathLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'s'
argument_list|)
operator|.
name|append
argument_list|(
name|fieldPostamble
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|retval
operator|.
name|append
argument_list|(
name|fieldPreamble
argument_list|)
operator|.
name|append
argument_list|(
literal|"%-"
argument_list|)
operator|.
name|append
argument_list|(
name|uriTemplateLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|uriTemplateLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'s'
argument_list|)
operator|.
name|append
argument_list|(
name|fieldPostamble
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|retval
operator|.
name|append
argument_list|(
name|fieldPreamble
argument_list|)
operator|.
name|append
argument_list|(
literal|"%-"
argument_list|)
operator|.
name|append
argument_list|(
name|methodLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|methodLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'s'
argument_list|)
operator|.
name|append
argument_list|(
name|fieldPostamble
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|retval
operator|.
name|append
argument_list|(
name|fieldPreamble
argument_list|)
operator|.
name|append
argument_list|(
literal|"%-"
argument_list|)
operator|.
name|append
argument_list|(
name|statusLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|statusLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'s'
argument_list|)
operator|.
name|append
argument_list|(
name|fieldPostamble
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|retval
operator|.
name|append
argument_list|(
name|fieldPreamble
argument_list|)
operator|.
name|append
argument_list|(
literal|"%-"
argument_list|)
operator|.
name|append
argument_list|(
name|routeLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|routeLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'s'
argument_list|)
operator|.
name|append
argument_list|(
name|fieldPostamble
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
return|return
name|retval
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getMaxColumnWidth ()
specifier|private
name|int
name|getMaxColumnWidth
parameter_list|()
block|{
if|if
condition|(
name|verbose
condition|)
block|{
return|return
name|Integer
operator|.
name|MAX_VALUE
return|;
block|}
else|else
block|{
return|return
name|MAX_COLUMN_WIDTH
return|;
block|}
block|}
block|}
end_class

end_unit

