begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * List the Camel validators available in the JVM.  */
end_comment

begin_class
DECL|class|ValidatorListCommand
specifier|public
class|class
name|ValidatorListCommand
extends|extends
name|AbstractCamelCommand
block|{
DECL|field|CONTEXT_NAME_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|CONTEXT_NAME_COLUMN_LABEL
init|=
literal|"Context"
decl_stmt|;
DECL|field|TYPE_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_COLUMN_LABEL
init|=
literal|"Type"
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
DECL|field|DESCRIPTION_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|DESCRIPTION_COLUMN_LABEL
init|=
literal|"Description"
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
DECL|field|explain
name|boolean
name|explain
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|String
name|context
decl_stmt|;
DECL|method|ValidatorListCommand (String context, boolean decode, boolean verbose, boolean explain)
specifier|public
name|ValidatorListCommand
parameter_list|(
name|String
name|context
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
name|this
operator|.
name|context
operator|=
name|context
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
specifier|final
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|camelContextInfos
init|=
name|camelController
operator|.
name|getCamelContexts
argument_list|(
name|this
operator|.
name|context
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|>
name|contextsToValidators
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|camelContextInfo
range|:
name|camelContextInfos
control|)
block|{
name|String
name|camelContextName
init|=
name|camelContextInfo
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|validators
init|=
name|camelController
operator|.
name|getValidators
argument_list|(
name|camelContextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|validators
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|contextsToValidators
operator|.
name|put
argument_list|(
name|camelContextName
argument_list|,
name|validators
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
name|columnWidths
init|=
name|computeColumnWidths
argument_list|(
name|contextsToValidators
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|>
name|stringListEntry
range|:
name|contextsToValidators
operator|.
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|String
name|camelContextName
init|=
name|stringListEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|validators
init|=
name|stringListEntry
operator|.
name|getValue
argument_list|()
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
name|headerFormat
argument_list|,
name|CONTEXT_NAME_COLUMN_LABEL
argument_list|,
name|TYPE_COLUMN_LABEL
argument_list|,
name|STATE_COLUMN_LABEL
argument_list|,
name|DESCRIPTION_COLUMN_LABEL
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
literal|"----"
argument_list|,
literal|"-----"
argument_list|,
literal|"-----------"
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
name|CONTEXT_NAME_COLUMN_LABEL
argument_list|,
name|TYPE_COLUMN_LABEL
argument_list|,
name|STATE_COLUMN_LABEL
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
literal|"----"
argument_list|,
literal|"-----"
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
name|validators
control|)
block|{
name|String
name|type
init|=
name|row
operator|.
name|get
argument_list|(
literal|"type"
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
if|if
condition|(
name|verbose
condition|)
block|{
name|String
name|desc
init|=
name|row
operator|.
name|get
argument_list|(
literal|"description"
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
name|camelContextName
argument_list|,
name|type
argument_list|,
name|state
argument_list|,
name|desc
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
name|camelContextName
argument_list|,
name|type
argument_list|,
name|state
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
DECL|method|computeColumnWidths (final Map<String, List<Map<String, String>>> contextsToValidators)
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
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|>
name|contextsToValidators
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|maxCamelContextLen
init|=
literal|0
decl_stmt|;
name|int
name|maxTypeLen
init|=
literal|0
decl_stmt|;
name|int
name|maxStatusLen
init|=
literal|0
decl_stmt|;
name|int
name|maxDescLen
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|>
name|stringListEntry
range|:
name|contextsToValidators
operator|.
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|String
name|camelContextName
init|=
name|stringListEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|maxCamelContextLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxCamelContextLen
argument_list|,
name|camelContextName
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|validators
init|=
name|stringListEntry
operator|.
name|getValue
argument_list|()
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
name|validators
control|)
block|{
name|String
name|type
init|=
name|row
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|maxTypeLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxTypeLen
argument_list|,
name|type
operator|==
literal|null
condition|?
literal|0
else|:
name|type
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
if|if
condition|(
name|verbose
condition|)
block|{
name|String
name|desc
init|=
name|row
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
decl_stmt|;
name|maxDescLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxDescLen
argument_list|,
name|desc
operator|==
literal|null
condition|?
literal|0
else|:
name|desc
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|CONTEXT_NAME_COLUMN_LABEL
argument_list|,
name|maxCamelContextLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|TYPE_COLUMN_LABEL
argument_list|,
name|maxTypeLen
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
if|if
condition|(
name|verbose
condition|)
block|{
name|retval
operator|.
name|put
argument_list|(
name|DESCRIPTION_COLUMN_LABEL
argument_list|,
name|maxDescLen
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
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
name|ctxLen
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
name|CONTEXT_NAME_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
decl_stmt|;
name|ctxLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|ctxLen
argument_list|)
expr_stmt|;
name|int
name|typeLen
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
name|TYPE_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
decl_stmt|;
name|typeLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|typeLen
argument_list|)
expr_stmt|;
name|int
name|stateLen
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|verbose
condition|)
block|{
name|stateLen
operator|=
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
name|STATE_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|()
argument_list|)
expr_stmt|;
name|stateLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|stateLen
argument_list|)
expr_stmt|;
block|}
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
name|ctxLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|ctxLen
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
name|typeLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|typeLen
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
if|if
condition|(
name|verbose
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
name|stateLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|stateLen
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
literal|"%s"
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

