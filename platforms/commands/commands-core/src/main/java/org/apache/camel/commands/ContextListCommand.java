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
comment|/**  * Command to list all {@link org.apache.camel.CamelContext} in the JVM.  */
end_comment

begin_class
DECL|class|ContextListCommand
specifier|public
class|class
name|ContextListCommand
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
DECL|field|STATUS_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|STATUS_COLUMN_LABEL
init|=
literal|"Status"
decl_stmt|;
DECL|field|UPTIME_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|UPTIME_COLUMN_LABEL
init|=
literal|"Uptime"
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
DECL|field|DEFAULT_COLUMN_WIDTH_INCREMENT
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_COLUMN_WIDTH_INCREMENT
init|=
literal|0
decl_stmt|;
DECL|field|MAX_COLUMN_WIDTH
specifier|private
specifier|static
specifier|final
name|int
name|MAX_COLUMN_WIDTH
init|=
name|Integer
operator|.
name|MAX_VALUE
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
name|camelContexts
init|=
name|camelController
operator|.
name|getCamelContexts
argument_list|()
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
name|camelContexts
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
name|camelContexts
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
name|STATUS_COLUMN_LABEL
argument_list|,
name|UPTIME_COLUMN_LABEL
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
literal|"------"
argument_list|,
literal|"------"
argument_list|)
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
name|row
range|:
name|camelContexts
control|)
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
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"state"
argument_list|)
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"uptime"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|computeColumnWidths (final Iterable<Map<String, String>> camelContexts)
specifier|private
specifier|static
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|camelContexts
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContexts
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to determine column widths from null Iterable<CamelContext>"
argument_list|)
throw|;
block|}
else|else
block|{
name|int
name|maxNameLen
init|=
literal|0
decl_stmt|;
name|int
name|maxStatusLen
init|=
literal|0
decl_stmt|;
name|int
name|maxUptimeLen
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
name|camelContexts
control|)
block|{
specifier|final
name|String
name|name
init|=
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|maxNameLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxNameLen
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
specifier|final
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
specifier|final
name|String
name|uptime
init|=
name|row
operator|.
name|get
argument_list|(
literal|"uptime"
argument_list|)
decl_stmt|;
name|maxUptimeLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxUptimeLen
argument_list|,
name|uptime
operator|==
literal|null
condition|?
literal|0
else|:
name|uptime
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
name|maxNameLen
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
name|retval
operator|.
name|put
argument_list|(
name|UPTIME_COLUMN_LABEL
argument_list|,
name|maxUptimeLen
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
block|}
DECL|method|buildFormatString (final Map<String, Integer> columnWidths, final boolean isHeader)
specifier|private
specifier|static
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
name|MAX_COLUMN_WIDTH
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
name|MAX_COLUMN_WIDTH
argument_list|)
decl_stmt|;
name|int
name|uptimeLen
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
name|UPTIME_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|MAX_COLUMN_WIDTH
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
name|uptimeLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|uptimeLen
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
block|}
end_class

end_unit

