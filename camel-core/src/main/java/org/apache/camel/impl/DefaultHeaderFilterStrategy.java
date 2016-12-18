begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|Exchange
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
name|HeaderFilterStrategy
import|;
end_import

begin_comment
comment|/**  * The default header filtering strategy. Users can configure filter by  * setting filter set and/or setting a regular expression. Subclass can  * add extended filter logic in   * {@link #extendedFilter(org.apache.camel.spi.HeaderFilterStrategy.Direction, String, Object, org.apache.camel.Exchange)}  *   * Filters are associated with directions (in or out). "In" direction is  * referred to propagating headers "to" Camel message. The "out" direction  * is opposite which is referred to propagating headers from Camel message  * to a native message like JMS and CXF message. You can see example of  * DefaultHeaderFilterStrategy are being extended and invoked in camel-jms   * and camel-cxf components.  *  * @version   */
end_comment

begin_class
DECL|class|DefaultHeaderFilterStrategy
specifier|public
class|class
name|DefaultHeaderFilterStrategy
implements|implements
name|HeaderFilterStrategy
block|{
DECL|field|inFilter
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|inFilter
decl_stmt|;
DECL|field|inFilterPattern
specifier|private
name|Pattern
name|inFilterPattern
decl_stmt|;
DECL|field|outFilter
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|outFilter
decl_stmt|;
DECL|field|outFilterPattern
specifier|private
name|Pattern
name|outFilterPattern
decl_stmt|;
DECL|field|lowerCase
specifier|private
name|boolean
name|lowerCase
decl_stmt|;
DECL|field|allowNullValues
specifier|private
name|boolean
name|allowNullValues
decl_stmt|;
DECL|field|caseInsensitive
specifier|private
name|boolean
name|caseInsensitive
decl_stmt|;
DECL|method|applyFilterToCamelHeaders (String headerName, Object headerValue, Exchange exchange)
specifier|public
name|boolean
name|applyFilterToCamelHeaders
parameter_list|(
name|String
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|doFiltering
argument_list|(
name|Direction
operator|.
name|OUT
argument_list|,
name|headerName
argument_list|,
name|headerValue
argument_list|,
name|exchange
argument_list|)
return|;
block|}
DECL|method|applyFilterToExternalHeaders (String headerName, Object headerValue, Exchange exchange)
specifier|public
name|boolean
name|applyFilterToExternalHeaders
parameter_list|(
name|String
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|doFiltering
argument_list|(
name|Direction
operator|.
name|IN
argument_list|,
name|headerName
argument_list|,
name|headerValue
argument_list|,
name|exchange
argument_list|)
return|;
block|}
comment|/**      * Gets the "out" direction filter set. The "out" direction is referred to      * copying headers from a Camel message to an external message.      *       * @return a set that contains header names that should be excluded.      */
DECL|method|getOutFilter ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getOutFilter
parameter_list|()
block|{
if|if
condition|(
name|outFilter
operator|==
literal|null
condition|)
block|{
name|outFilter
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|outFilter
return|;
block|}
comment|/**      * Sets the "out" direction filter set. The "out" direction is referred to      * copying headers from a Camel message to an external message.      *      * @param value  the filter      */
DECL|method|setOutFilter (Set<String> value)
specifier|public
name|void
name|setOutFilter
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|value
parameter_list|)
block|{
name|outFilter
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the "out" direction filter regular expression {@link Pattern}. The      * "out" direction is referred to copying headers from Camel message to      * an external message. If the pattern matches a header, the header will      * be filtered out.       *       * @return regular expression filter pattern      */
DECL|method|getOutFilterPattern ()
specifier|public
name|String
name|getOutFilterPattern
parameter_list|()
block|{
return|return
name|outFilterPattern
operator|==
literal|null
condition|?
literal|null
else|:
name|outFilterPattern
operator|.
name|pattern
argument_list|()
return|;
block|}
comment|/**      * Sets the "out" direction filter regular expression {@link Pattern}. The      * "out" direction is referred to copying headers from Camel message to      * an external message. If the pattern matches a header, the header will      * be filtered out.       *       * @param value regular expression filter pattern      */
DECL|method|setOutFilterPattern (String value)
specifier|public
name|void
name|setOutFilterPattern
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|outFilterPattern
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|outFilterPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets the "in" direction filter set. The "in" direction is referred to      * copying headers from an external message to a Camel message.      *       * @return a set that contains header names that should be excluded.      */
DECL|method|getInFilter ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getInFilter
parameter_list|()
block|{
if|if
condition|(
name|inFilter
operator|==
literal|null
condition|)
block|{
name|inFilter
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|inFilter
return|;
block|}
comment|/**      * Sets the "in" direction filter set. The "in" direction is referred to      * copying headers from an external message to a Camel message.      *      * @param value the filter      */
DECL|method|setInFilter (Set<String> value)
specifier|public
name|void
name|setInFilter
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|value
parameter_list|)
block|{
name|inFilter
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the "in" direction filter regular expression {@link Pattern}. The      * "in" direction is referred to copying headers from an external message      * to a Camel message. If the pattern matches a header, the header will      * be filtered out.       *       * @return regular expression filter pattern      */
DECL|method|getInFilterPattern ()
specifier|public
name|String
name|getInFilterPattern
parameter_list|()
block|{
return|return
name|inFilterPattern
operator|==
literal|null
condition|?
literal|null
else|:
name|inFilterPattern
operator|.
name|pattern
argument_list|()
return|;
block|}
comment|/**      * Sets the "in" direction filter regular expression {@link Pattern}. The      * "in" direction is referred to copying headers from an external message      * to a Camel message. If the pattern matches a header, the header will      * be filtered out.       *       * @param value regular expression filter pattern      */
DECL|method|setInFilterPattern (String value)
specifier|public
name|void
name|setInFilterPattern
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|inFilterPattern
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|inFilterPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets the isLowercase property which is a boolean to determine      * whether header names should be converted to lower case before      * checking it with the filter Set. It does not affect filtering using      * regular expression pattern.      */
DECL|method|isLowerCase ()
specifier|public
name|boolean
name|isLowerCase
parameter_list|()
block|{
return|return
name|lowerCase
return|;
block|}
comment|/**      * Sets the isLowercase property which is a boolean to determine      * whether header names should be converted to lower case before      * checking it with the filter Set. It does not affect filtering using      * regular expression pattern.      */
DECL|method|setLowerCase (boolean value)
specifier|public
name|void
name|setLowerCase
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|lowerCase
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Gets the caseInsensitive property which is a boolean to determine      * whether header names should be case insensitive when checking it       * with the filter set.      * It does not affect filtering using regular expression pattern.      *       * @return<tt>true</tt> if header names is case insensitive.       */
DECL|method|isCaseInsensitive ()
specifier|public
name|boolean
name|isCaseInsensitive
parameter_list|()
block|{
return|return
name|caseInsensitive
return|;
block|}
comment|/**      * Sets the caseInsensitive property which is a boolean to determine      * whether header names should be case insensitive when checking it       * with the filter set.      * It does not affect filtering using regular expression pattern,      *       * @param caseInsensitive<tt>true</tt> if header names is case insensitive.      */
DECL|method|setCaseInsensitive (boolean caseInsensitive)
specifier|public
name|void
name|setCaseInsensitive
parameter_list|(
name|boolean
name|caseInsensitive
parameter_list|)
block|{
name|this
operator|.
name|caseInsensitive
operator|=
name|caseInsensitive
expr_stmt|;
block|}
DECL|method|isAllowNullValues ()
specifier|public
name|boolean
name|isAllowNullValues
parameter_list|()
block|{
return|return
name|allowNullValues
return|;
block|}
DECL|method|setAllowNullValues (boolean value)
specifier|public
name|void
name|setAllowNullValues
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|allowNullValues
operator|=
name|value
expr_stmt|;
block|}
DECL|method|extendedFilter (Direction direction, String key, Object value, Exchange exchange)
specifier|protected
name|boolean
name|extendedFilter
parameter_list|(
name|Direction
name|direction
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
DECL|method|doFiltering (Direction direction, String headerName, Object headerValue, Exchange exchange)
specifier|private
name|boolean
name|doFiltering
parameter_list|(
name|Direction
name|direction
parameter_list|,
name|String
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|headerName
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|headerValue
operator|==
literal|null
operator|&&
operator|!
name|allowNullValues
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Pattern
name|pattern
init|=
literal|null
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|filter
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Direction
operator|.
name|OUT
operator|==
name|direction
condition|)
block|{
name|pattern
operator|=
name|outFilterPattern
expr_stmt|;
name|filter
operator|=
name|outFilter
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Direction
operator|.
name|IN
operator|==
name|direction
condition|)
block|{
name|pattern
operator|=
name|inFilterPattern
expr_stmt|;
name|filter
operator|=
name|inFilter
expr_stmt|;
block|}
if|if
condition|(
name|pattern
operator|!=
literal|null
operator|&&
name|pattern
operator|.
name|matcher
argument_list|(
name|headerName
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isCaseInsensitive
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|filterString
range|:
name|filter
control|)
block|{
if|if
condition|(
name|filterString
operator|.
name|equalsIgnoreCase
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|isLowerCase
argument_list|()
condition|)
block|{
if|if
condition|(
name|filter
operator|.
name|contains
argument_list|(
name|headerName
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|filter
operator|.
name|contains
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
name|extendedFilter
argument_list|(
name|direction
argument_list|,
name|headerName
argument_list|,
name|headerValue
argument_list|,
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

