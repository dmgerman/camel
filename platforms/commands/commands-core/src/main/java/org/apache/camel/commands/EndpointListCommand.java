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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|Endpoint
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
name|ServiceStatus
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
name|StatefulService
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
name|JsonSchemaHelper
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
comment|/**  * List the Camel endpoints available in the JVM.  */
end_comment

begin_class
DECL|class|EndpointListCommand
specifier|public
class|class
name|EndpointListCommand
extends|extends
name|AbstractCamelCommand
block|{
DECL|field|CONTEXT_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|CONTEXT_COLUMN_LABEL
init|=
literal|"Context"
decl_stmt|;
DECL|field|URI_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|URI_COLUMN_LABEL
init|=
literal|"Uri"
decl_stmt|;
DECL|field|STATUS_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|STATUS_COLUMN_LABEL
init|=
literal|"Status"
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
DECL|field|name
name|String
name|name
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
DECL|field|explain
name|boolean
name|explain
decl_stmt|;
DECL|method|EndpointListCommand (String name, boolean decode, boolean verbose, boolean explain)
specifier|public
name|EndpointListCommand
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|decode
parameter_list|,
name|boolean
name|verbose
parameter_list|,
name|boolean
name|explain
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
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
name|this
operator|.
name|explain
operator|=
name|explain
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|execute (CamelController camelController, PrintStream out, PrintStream err)
specifier|public
name|Object
name|execute
parameter_list|(
name|CamelController
name|camelController
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
name|Endpoint
argument_list|>
name|endpoints
init|=
name|camelController
operator|.
name|getEndpoints
argument_list|(
name|name
argument_list|)
decl_stmt|;
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
name|endpoints
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
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoints
operator|.
name|size
argument_list|()
operator|>
literal|0
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
name|CONTEXT_COLUMN_LABEL
argument_list|,
name|URI_COLUMN_LABEL
argument_list|,
name|STATUS_COLUMN_LABEL
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
literal|"-------"
argument_list|,
literal|"---"
argument_list|,
literal|"------"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Endpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
name|String
name|contextId
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
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
name|String
name|state
init|=
name|getEndpointState
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
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
name|contextId
argument_list|,
name|uri
argument_list|,
name|state
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|explain
condition|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
name|String
name|json
init|=
name|camelController
operator|.
name|explainEndpointAsJSon
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|verbose
argument_list|)
decl_stmt|;
comment|// use a basic json parser
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|options
init|=
name|JsonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"properties"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// lets sort the options by name
name|Collections
operator|.
name|sort
argument_list|(
name|options
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|o1
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|o2
parameter_list|)
block|{
comment|// sort by kind first (need to -1 as we want path on top), then name
name|int
name|answer
init|=
operator|-
literal|1
operator|*
name|o1
operator|.
name|get
argument_list|(
literal|"kind"
argument_list|)
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|get
argument_list|(
literal|"kind"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|0
condition|)
block|{
name|answer
operator|=
name|o1
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|option
range|:
name|options
control|)
block|{
name|String
name|key
init|=
name|option
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|kind
init|=
name|option
operator|.
name|get
argument_list|(
literal|"kind"
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|option
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|String
name|javaType
init|=
name|option
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|option
operator|.
name|get
argument_list|(
literal|"value"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|value
operator|=
name|option
operator|.
name|get
argument_list|(
literal|"defaultValue"
argument_list|)
expr_stmt|;
block|}
name|String
name|desc
init|=
name|option
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
name|String
name|line
decl_stmt|;
if|if
condition|(
literal|"path"
operator|.
name|equals
argument_list|(
name|kind
argument_list|)
condition|)
block|{
name|line
operator|=
literal|"\t"
operator|+
name|key
operator|+
literal|" (endpoint path) = "
operator|+
name|value
expr_stmt|;
block|}
else|else
block|{
name|line
operator|=
literal|"\t"
operator|+
name|key
operator|+
literal|" = "
operator|+
name|value
expr_stmt|;
block|}
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
literal|""
argument_list|,
name|line
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|String
name|displayType
init|=
name|type
decl_stmt|;
if|if
condition|(
name|javaType
operator|!=
literal|null
operator|&&
operator|!
name|displayType
operator|.
name|equals
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
name|displayType
operator|=
name|type
operator|+
literal|" ("
operator|+
name|javaType
operator|+
literal|")"
expr_stmt|;
block|}
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
literal|""
argument_list|,
literal|"\t"
operator|+
name|displayType
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|desc
operator|!=
literal|null
condition|)
block|{
comment|// TODO: split desc in multi lines so it does not overflow
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
literal|""
argument_list|,
literal|"\t"
operator|+
name|desc
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|computeColumnWidths (final Iterable<Endpoint> endpoints)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|computeColumnWidths
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|endpoints
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to determine column widths from null Iterable<Endpoint>"
argument_list|)
throw|;
block|}
else|else
block|{
name|int
name|maxContextLen
init|=
literal|0
decl_stmt|;
name|int
name|maxUriLen
init|=
literal|0
decl_stmt|;
name|int
name|maxStatusLen
init|=
literal|0
decl_stmt|;
for|for
control|(
specifier|final
name|Endpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
specifier|final
name|String
name|name
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|maxContextLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxContextLen
argument_list|,
name|name
operator|==
literal|null
condition|?
literal|0
else|:
name|name
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
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
name|java
operator|.
name|lang
operator|.
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
specifier|final
name|String
name|status
init|=
name|getEndpointState
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|maxStatusLen
operator|=
name|java
operator|.
name|lang
operator|.
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
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|CONTEXT_COLUMN_LABEL
argument_list|,
name|maxContextLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|URI_COLUMN_LABEL
argument_list|,
name|maxUriLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|STATUS_COLUMN_LABEL
argument_list|,
name|maxStatusLen
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
block|}
DECL|method|buildFormatString (final Map<String, Integer> columnWidths, final boolean isHeader)
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
name|contextLen
init|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|CONTEXT_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|uriLen
init|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|URI_COLUMN_LABEL
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
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|STATUS_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
decl_stmt|;
name|contextLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|contextLen
argument_list|)
expr_stmt|;
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
name|contextLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|contextLen
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
DECL|method|getEndpointState (Endpoint endpoint)
specifier|private
specifier|static
name|String
name|getEndpointState
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
comment|// must use String type to be sure remote JMX can read the attribute without requiring Camel classes.
if|if
condition|(
name|endpoint
operator|instanceof
name|StatefulService
condition|)
block|{
name|ServiceStatus
name|status
init|=
operator|(
operator|(
name|StatefulService
operator|)
name|endpoint
operator|)
operator|.
name|getStatus
argument_list|()
decl_stmt|;
return|return
name|status
operator|.
name|name
argument_list|()
return|;
block|}
comment|// assume started if not a ServiceSupport instance
return|return
name|ServiceStatus
operator|.
name|Started
operator|.
name|name
argument_list|()
return|;
block|}
block|}
end_class

end_unit

