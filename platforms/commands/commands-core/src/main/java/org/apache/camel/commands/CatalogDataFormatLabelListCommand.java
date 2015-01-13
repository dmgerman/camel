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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|CollectionStringBuffer
import|;
end_import

begin_comment
comment|/**  * From the Camel catalog lists all the data format labels.  */
end_comment

begin_class
DECL|class|CatalogDataFormatLabelListCommand
specifier|public
class|class
name|CatalogDataFormatLabelListCommand
extends|extends
name|AbstractCamelCommand
block|{
DECL|field|LABEL_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|LABEL_COLUMN_LABEL
init|=
literal|"Label"
decl_stmt|;
DECL|field|NUMBER_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|NUMBER_COLUMN_LABEL
init|=
literal|"#"
decl_stmt|;
DECL|field|NAME_COLUMN_LABEL
specifier|private
specifier|static
specifier|final
name|String
name|NAME_COLUMN_LABEL
init|=
literal|"Name"
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
comment|// descriptions can be very long so clip by default after 120 chars
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
DECL|field|MIN_NUMBER_COLUMN_WIDTH
specifier|private
specifier|static
specifier|final
name|int
name|MIN_NUMBER_COLUMN_WIDTH
init|=
literal|6
decl_stmt|;
DECL|field|verbose
specifier|private
name|boolean
name|verbose
decl_stmt|;
DECL|method|CatalogDataFormatLabelListCommand (boolean verbose)
specifier|public
name|CatalogDataFormatLabelListCommand
parameter_list|(
name|boolean
name|verbose
parameter_list|)
block|{
name|this
operator|.
name|verbose
operator|=
name|verbose
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
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|labels
init|=
name|camelController
operator|.
name|listDataFormatsLabelCatalog
argument_list|()
decl_stmt|;
if|if
condition|(
name|labels
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
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
name|labels
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
name|LABEL_COLUMN_LABEL
argument_list|,
name|NUMBER_COLUMN_LABEL
argument_list|,
name|NAME_COLUMN_LABEL
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
literal|"-----"
argument_list|,
literal|"-"
argument_list|,
literal|"----"
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
name|LABEL_COLUMN_LABEL
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
literal|"-----"
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|row
range|:
name|labels
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|verbose
condition|)
block|{
name|String
name|label
init|=
name|row
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|number
init|=
literal|""
operator|+
name|row
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|", "
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|row
operator|.
name|getValue
argument_list|()
control|)
block|{
name|csb
operator|.
name|append
argument_list|(
name|name
argument_list|)
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
name|label
argument_list|,
name|number
argument_list|,
name|csb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|label
init|=
name|row
operator|.
name|getKey
argument_list|()
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
name|label
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|computeColumnWidths (Map<String, Set<String>> labels)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|computeColumnWidths
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|labels
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|labels
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
comment|// some of the options is optional so we need to start from 1
name|int
name|maxLabelLen
init|=
name|LABEL_COLUMN_LABEL
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|maxNameLen
init|=
name|NAME_COLUMN_LABEL
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|", "
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
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|labels
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|label
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|maxLabelLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxLabelLen
argument_list|,
name|label
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|counter
operator|++
expr_stmt|;
name|csb
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|maxNameLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxNameLen
argument_list|,
name|csb
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|maxMumberLen
init|=
operator|(
literal|""
operator|+
name|counter
operator|)
operator|.
name|length
argument_list|()
decl_stmt|;
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
argument_list|()
decl_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|LABEL_COLUMN_LABEL
argument_list|,
name|maxLabelLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|NUMBER_COLUMN_LABEL
argument_list|,
name|maxMumberLen
argument_list|)
expr_stmt|;
name|retval
operator|.
name|put
argument_list|(
name|NAME_COLUMN_LABEL
argument_list|,
name|maxNameLen
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
block|}
DECL|method|buildFormatString (Map<String, Integer> columnWidths, boolean isHeader)
specifier|private
name|String
name|buildFormatString
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|columnWidths
parameter_list|,
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
if|if
condition|(
name|verbose
condition|)
block|{
name|int
name|labelLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|LABEL_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|(
name|isHeader
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|numberLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|NUMBER_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|(
name|isHeader
argument_list|)
argument_list|)
decl_stmt|;
name|labelLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|labelLen
argument_list|)
expr_stmt|;
name|numberLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_NUMBER_COLUMN_WIDTH
argument_list|,
name|numberLen
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
name|labelLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|labelLen
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
name|numberLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|numberLen
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
comment|// allow last to overlap
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
else|else
block|{
name|int
name|labelLen
init|=
name|Math
operator|.
name|min
argument_list|(
name|columnWidths
operator|.
name|get
argument_list|(
name|LABEL_COLUMN_LABEL
argument_list|)
operator|+
name|columnWidthIncrement
argument_list|,
name|getMaxColumnWidth
argument_list|(
name|isHeader
argument_list|)
argument_list|)
decl_stmt|;
name|labelLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|MIN_COLUMN_WIDTH
argument_list|,
name|labelLen
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
name|labelLen
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|labelLen
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
DECL|method|getMaxColumnWidth (boolean isHeader)
specifier|private
name|int
name|getMaxColumnWidth
parameter_list|(
name|boolean
name|isHeader
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isHeader
operator|&&
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

