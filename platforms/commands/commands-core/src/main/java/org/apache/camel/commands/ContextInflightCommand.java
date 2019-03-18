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
comment|/**  * Command to display inflight exchange information  */
end_comment

begin_class
DECL|class|ContextInflightCommand
specifier|public
class|class
name|ContextInflightCommand
extends|extends
name|AbstractContextCommand
block|{
DECL|field|EXCHANGE_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|EXCHANGE_COLUMN_LABEL
init|=
literal|"ExchangeId"
decl_stmt|;
DECL|field|FROM_ROUTE_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|FROM_ROUTE_COLUMN_LABEL
init|=
literal|"From Route"
decl_stmt|;
DECL|field|ROUTE_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|ROUTE_COLUMN_LABEL
init|=
literal|"Route"
decl_stmt|;
DECL|field|NODE_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|NODE_COLUMN_LABEL
init|=
literal|"Node"
decl_stmt|;
DECL|field|ELAPSED_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|ELAPSED_COLUMN_LABEL
init|=
literal|"Elapsed (ms)"
decl_stmt|;
DECL|field|DURATION_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|DURATION_COLUMN_LABEL
init|=
literal|"Duration (ms)"
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
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
DECL|field|route
specifier|private
name|String
name|route
decl_stmt|;
DECL|field|sortByLongestDuration
specifier|private
name|boolean
name|sortByLongestDuration
decl_stmt|;
DECL|method|ContextInflightCommand (String context, String route, int limit, boolean sortByLongestDuration)
specifier|public
name|ContextInflightCommand
parameter_list|(
name|String
name|context
parameter_list|,
name|String
name|route
parameter_list|,
name|int
name|limit
parameter_list|,
name|boolean
name|sortByLongestDuration
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
name|this
operator|.
name|sortByLongestDuration
operator|=
name|sortByLongestDuration
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
name|Object
argument_list|>
argument_list|>
name|inflight
init|=
name|camelController
operator|.
name|browseInflightExchanges
argument_list|(
name|contextName
argument_list|,
name|route
argument_list|,
name|limit
argument_list|,
name|sortByLongestDuration
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
name|inflight
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
name|inflight
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
name|EXCHANGE_COLUMN_LABEL
argument_list|,
name|FROM_ROUTE_COLUMN_LABEL
argument_list|,
name|ROUTE_COLUMN_LABEL
argument_list|,
name|NODE_COLUMN_LABEL
argument_list|,
name|ELAPSED_COLUMN_LABEL
argument_list|,
name|DURATION_COLUMN_LABEL
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
literal|"----------"
argument_list|,
literal|"----------"
argument_list|,
literal|"-----"
argument_list|,
literal|"----"
argument_list|,
literal|"------------"
argument_list|,
literal|"-------------"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
range|:
name|inflight
control|)
block|{
name|Object
name|exchangeId
init|=
name|row
operator|.
name|get
argument_list|(
literal|"exchangeId"
argument_list|)
decl_stmt|;
name|Object
name|fromRouteId
init|=
name|row
operator|.
name|get
argument_list|(
literal|"fromRouteId"
argument_list|)
decl_stmt|;
name|Object
name|routeId
init|=
name|row
operator|.
name|get
argument_list|(
literal|"routeId"
argument_list|)
decl_stmt|;
name|Object
name|nodeId
init|=
name|row
operator|.
name|get
argument_list|(
literal|"nodeId"
argument_list|)
decl_stmt|;
name|Object
name|elapsed
init|=
name|row
operator|.
name|get
argument_list|(
literal|"elapsed"
argument_list|)
decl_stmt|;
name|Object
name|duration
init|=
name|row
operator|.
name|get
argument_list|(
literal|"duration"
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
name|exchangeId
argument_list|,
name|fromRouteId
argument_list|,
name|routeId
argument_list|,
name|nodeId
argument_list|,
name|safeNull
argument_list|(
name|elapsed
argument_list|)
argument_list|,
name|safeNull
argument_list|(
name|duration
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
DECL|method|computeColumnWidths (final Iterable<Map<String, Object>> inflight)
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|inflight
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|inflight
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to determine column widths from null Iterable<Inflight>"
argument_list|)
throw|;
block|}
else|else
block|{
name|int
name|maxExchangeLen
init|=
literal|0
decl_stmt|;
name|int
name|maxFromRouteLen
init|=
literal|0
decl_stmt|;
name|int
name|maxRouteLen
init|=
literal|0
decl_stmt|;
name|int
name|maxNodeLen
init|=
literal|0
decl_stmt|;
name|int
name|maxElapsedLen
init|=
literal|0
decl_stmt|;
name|int
name|maxDurationLen
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
range|:
name|inflight
control|)
block|{
specifier|final
name|String
name|exchangeId
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"exchangeId"
argument_list|)
argument_list|)
decl_stmt|;
name|maxExchangeLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxExchangeLen
argument_list|,
name|exchangeId
operator|==
literal|null
condition|?
literal|0
else|:
name|exchangeId
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|fromRouteId
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"fromRouteId"
argument_list|)
argument_list|)
decl_stmt|;
name|maxFromRouteLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxFromRouteLen
argument_list|,
name|fromRouteId
operator|==
literal|null
condition|?
literal|0
else|:
name|fromRouteId
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|routeId
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"routeId"
argument_list|)
argument_list|)
decl_stmt|;
name|maxRouteLen
operator|=
name|java
operator|.
name|lang
operator|.
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
specifier|final
name|String
name|nodeId
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"nodeId"
argument_list|)
argument_list|)
decl_stmt|;
name|maxNodeLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxNodeLen
argument_list|,
name|nodeId
operator|==
literal|null
condition|?
literal|0
else|:
name|nodeId
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|elapsed
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"elapsed"
argument_list|)
argument_list|)
decl_stmt|;
name|maxElapsedLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxElapsedLen
argument_list|,
name|elapsed
operator|==
literal|null
condition|?
literal|0
else|:
name|elapsed
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|duration
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"duration"
argument_list|)
argument_list|)
decl_stmt|;
name|maxDurationLen
operator|=
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
argument_list|(
name|maxDurationLen
argument_list|,
name|duration
operator|==
literal|null
condition|?
literal|0
else|:
name|duration
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
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|EXCHANGE_COLUMN_LABEL
argument_list|,
name|maxExchangeLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|FROM_ROUTE_COLUMN_LABEL
argument_list|,
name|maxFromRouteLen
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
name|retval
operator|.
name|put
argument_list|(
name|NODE_COLUMN_LABEL
argument_list|,
name|maxNodeLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|ELAPSED_COLUMN_LABEL
argument_list|,
name|maxElapsedLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|DURATION_COLUMN_LABEL
argument_list|,
name|maxDurationLen
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
name|exchangeLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|EXCHANGE_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|MAX_COLUMN_WIDTH
argument_list|)
decl_stmt|;
name|int
name|fromRouteLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|FROM_ROUTE_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|MAX_COLUMN_WIDTH
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
name|MAX_COLUMN_WIDTH
argument_list|)
decl_stmt|;
name|int
name|nodeLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|NODE_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|MAX_COLUMN_WIDTH
argument_list|)
decl_stmt|;
name|int
name|elapsedLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|ELAPSED_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|MAX_COLUMN_WIDTH
argument_list|)
decl_stmt|;
name|int
name|durationLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|DURATION_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|MAX_COLUMN_WIDTH
argument_list|)
decl_stmt|;
name|exchangeLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|exchangeLen
argument_list|)
expr_stmt|;
name|fromRouteLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|fromRouteLen
argument_list|)
expr_stmt|;
name|routeLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|routeLen
argument_list|)
expr_stmt|;
name|nodeLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|nodeLen
argument_list|)
expr_stmt|;
name|elapsedLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|elapsedLen
argument_list|)
expr_stmt|;
name|durationLen
operator|=
name|Math
operator|.
name|max
argument_list|(
literal|13
argument_list|,
name|durationLen
argument_list|)
expr_stmt|;
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
name|exchangeLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|exchangeLen
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
name|fromRouteLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|fromRouteLen
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
name|nodeLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|nodeLen
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
literal|"%"
argument_list|)
operator|.
name|append
argument_list|(
name|elapsedLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|elapsedLen
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
literal|"%"
argument_list|)
operator|.
name|append
argument_list|(
name|durationLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|durationLen
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

