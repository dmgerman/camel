begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.univocity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|univocity
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|univocity
operator|.
name|parsers
operator|.
name|common
operator|.
name|AbstractParser
import|;
end_import

begin_comment
comment|/**  * This class unmarshalls the exchange body using an uniVocity parser.  *  * @param<P> Parser class  */
end_comment

begin_class
DECL|class|Unmarshaller
specifier|final
class|class
name|Unmarshaller
parameter_list|<
name|P
extends|extends
name|AbstractParser
parameter_list|<
name|?
parameter_list|>
parameter_list|>
block|{
DECL|field|lazyLoad
specifier|private
specifier|final
name|boolean
name|lazyLoad
decl_stmt|;
DECL|field|asMap
specifier|private
specifier|final
name|boolean
name|asMap
decl_stmt|;
comment|/**      * Creates a new instance.      *      * @param lazyLoad whether or not the lines must be lazily read      * @param asMap    whether or not we must produce maps instead of lists for each row      */
DECL|method|Unmarshaller (boolean lazyLoad, boolean asMap)
name|Unmarshaller
parameter_list|(
name|boolean
name|lazyLoad
parameter_list|,
name|boolean
name|asMap
parameter_list|)
block|{
name|this
operator|.
name|lazyLoad
operator|=
name|lazyLoad
expr_stmt|;
name|this
operator|.
name|asMap
operator|=
name|asMap
expr_stmt|;
block|}
comment|/**      * Unmarshal from the given reader.      *      * @param reader             reader to read from      * @param parser             uniVocity parser to use      * @param headerRowProcessor Row processor that retrieves the header      * @return Unmarshalled data      */
DECL|method|unmarshal (Reader reader, P parser, HeaderRowProcessor headerRowProcessor)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Reader
name|reader
parameter_list|,
name|P
name|parser
parameter_list|,
name|HeaderRowProcessor
name|headerRowProcessor
parameter_list|)
block|{
name|parser
operator|.
name|beginParsing
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
init|=
name|asMap
condition|?
operator|new
name|MapRowIterator
argument_list|<
name|P
argument_list|>
argument_list|(
name|parser
argument_list|,
name|headerRowProcessor
argument_list|)
else|:
operator|new
name|ListRowIterator
argument_list|<
name|P
argument_list|>
argument_list|(
name|parser
argument_list|)
decl_stmt|;
return|return
name|lazyLoad
condition|?
name|iterator
else|:
name|convertToList
argument_list|(
name|iterator
argument_list|)
return|;
block|}
comment|/**      * Converts the given iterator into a list.      *      * @param iterator iterator to convert      * @param<T>      item class      * @return a list that contains all the items of the iterator      */
DECL|method|convertToList (Iterator<T> iterator)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|convertToList
parameter_list|(
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * This abstract class helps iterating over the rows using uniVocity.      *      * @param<E> Row class      * @param<P> Parser class      */
DECL|class|RowIterator
specifier|private
specifier|abstract
specifier|static
class|class
name|RowIterator
parameter_list|<
name|E
parameter_list|,
name|P
extends|extends
name|AbstractParser
parameter_list|<
name|?
parameter_list|>
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|E
argument_list|>
block|{
DECL|field|parser
specifier|private
specifier|final
name|P
name|parser
decl_stmt|;
DECL|field|row
specifier|private
name|String
index|[]
name|row
decl_stmt|;
comment|/**          * Creates a new instance.          *          * @param parser parser to use          */
DECL|method|RowIterator (P parser)
specifier|protected
name|RowIterator
parameter_list|(
name|P
name|parser
parameter_list|)
block|{
name|this
operator|.
name|parser
operator|=
name|parser
expr_stmt|;
name|row
operator|=
name|this
operator|.
name|parser
operator|.
name|parseNext
argument_list|()
expr_stmt|;
block|}
comment|/**          * {@inheritDoc}          */
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
specifier|final
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|row
operator|!=
literal|null
return|;
block|}
comment|/**          * {@inheritDoc}          */
annotation|@
name|Override
DECL|method|next ()
specifier|public
specifier|final
name|E
name|next
parameter_list|()
block|{
if|if
condition|(
name|row
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|E
name|result
init|=
name|convertRow
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|row
operator|=
name|parser
operator|.
name|parseNext
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**          * Warning: it always throws an {@code UnsupportedOperationException}          */
annotation|@
name|Override
DECL|method|remove ()
specifier|public
specifier|final
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**          * Converts the rows into the expected object.          *          * @param row row to convert          * @return converted row          */
DECL|method|convertRow (String[] row)
specifier|protected
specifier|abstract
name|E
name|convertRow
parameter_list|(
name|String
index|[]
name|row
parameter_list|)
function_decl|;
block|}
comment|/**      * This class is an iterator that transforms each row into a List.      *      * @param<P> Parser class      */
DECL|class|ListRowIterator
specifier|private
specifier|static
specifier|final
class|class
name|ListRowIterator
parameter_list|<
name|P
extends|extends
name|AbstractParser
parameter_list|<
name|?
parameter_list|>
parameter_list|>
extends|extends
name|RowIterator
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|,
name|P
argument_list|>
block|{
comment|/**          * Creates a new instance.          *          * @param parser parser to use          */
DECL|method|ListRowIterator (P parser)
specifier|protected
name|ListRowIterator
parameter_list|(
name|P
name|parser
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|)
expr_stmt|;
block|}
comment|/**          * {@inheritDoc}          */
annotation|@
name|Override
DECL|method|convertRow (String[] row)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|convertRow
parameter_list|(
name|String
index|[]
name|row
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|row
argument_list|)
return|;
block|}
block|}
comment|/**      * This class is an iterator that transform each row into a Map.      *      * @param<P> Parser class      */
DECL|class|MapRowIterator
specifier|private
specifier|static
class|class
name|MapRowIterator
parameter_list|<
name|P
extends|extends
name|AbstractParser
parameter_list|<
name|?
parameter_list|>
parameter_list|>
extends|extends
name|RowIterator
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|,
name|P
argument_list|>
block|{
DECL|field|headerRowProcessor
specifier|private
specifier|final
name|HeaderRowProcessor
name|headerRowProcessor
decl_stmt|;
comment|/**          * Creates a new instance          *          * @param parser             parser to use          * @param headerRowProcessor row processor to use in order to retrieve the headers          */
DECL|method|MapRowIterator (P parser, HeaderRowProcessor headerRowProcessor)
specifier|protected
name|MapRowIterator
parameter_list|(
name|P
name|parser
parameter_list|,
name|HeaderRowProcessor
name|headerRowProcessor
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|)
expr_stmt|;
name|this
operator|.
name|headerRowProcessor
operator|=
name|headerRowProcessor
expr_stmt|;
block|}
comment|/**          * {@inheritDoc}          */
annotation|@
name|Override
DECL|method|convertRow (String[] row)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|convertRow
parameter_list|(
name|String
index|[]
name|row
parameter_list|)
block|{
name|String
index|[]
name|headers
init|=
name|headerRowProcessor
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|Math
operator|.
name|min
argument_list|(
name|row
operator|.
name|length
argument_list|,
name|headers
operator|.
name|length
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|size
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|headers
index|[
name|i
index|]
argument_list|,
name|row
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

