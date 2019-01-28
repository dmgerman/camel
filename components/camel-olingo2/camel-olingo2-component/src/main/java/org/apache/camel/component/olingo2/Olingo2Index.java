begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 Red Hat, Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
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
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|ep
operator|.
name|entry
operator|.
name|EntryMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|ep
operator|.
name|entry
operator|.
name|ODataEntry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|ep
operator|.
name|feed
operator|.
name|ODataFeed
import|;
end_import

begin_class
DECL|class|Olingo2Index
specifier|public
class|class
name|Olingo2Index
block|{
DECL|field|resultIndex
specifier|private
name|Set
argument_list|<
name|Integer
argument_list|>
name|resultIndex
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Hash only certain data since other parts change between message      * exchanges.      *      * @param metadata      * @return hashcode of metadata      */
DECL|method|hash (EntryMetadata metadata)
specifier|private
name|int
name|hash
parameter_list|(
name|EntryMetadata
name|metadata
parameter_list|)
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|metadata
operator|.
name|getId
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|metadata
operator|.
name|getId
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|metadata
operator|.
name|getUri
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|metadata
operator|.
name|getUri
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Hash entry leaving out certain fields that change      * between exchange messages      *      * @param entry      * @return hascode of entry      */
DECL|method|hash (ODataEntry entry)
specifier|private
name|int
name|hash
parameter_list|(
name|ODataEntry
name|entry
parameter_list|)
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
comment|// Hash metadata to ignore certain entries
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|entry
operator|.
name|getMetadata
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|hash
argument_list|(
name|entry
operator|.
name|getMetadata
argument_list|()
argument_list|)
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|entry
operator|.
name|getProperties
argument_list|()
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|entry
operator|.
name|getProperties
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
comment|// Ignore mediaMetadata, expandSelectTree since its object changes each time
return|return
name|result
return|;
block|}
DECL|method|filter (Object o)
specifier|private
name|Object
name|filter
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|resultIndex
operator|.
name|contains
argument_list|(
name|o
operator|.
name|hashCode
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|o
return|;
block|}
DECL|method|indexDefault (Object o)
specifier|private
name|void
name|indexDefault
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|resultIndex
operator|.
name|add
argument_list|(
name|o
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|filter (Iterable<?> iterable)
specifier|private
name|Iterable
argument_list|<
name|?
argument_list|>
name|filter
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|iterable
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|filtered
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|iterable
control|)
block|{
if|if
condition|(
name|resultIndex
operator|.
name|contains
argument_list|(
name|o
operator|.
name|hashCode
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|filtered
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
return|return
name|filtered
return|;
block|}
DECL|method|index (Iterable<?> iterable)
specifier|private
name|void
name|index
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|iterable
parameter_list|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|iterable
control|)
block|{
name|resultIndex
operator|.
name|add
argument_list|(
name|o
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|filter (ODataFeed odataFeed)
specifier|private
name|ODataFeed
name|filter
parameter_list|(
name|ODataFeed
name|odataFeed
parameter_list|)
block|{
name|List
argument_list|<
name|ODataEntry
argument_list|>
name|entries
init|=
name|odataFeed
operator|.
name|getEntries
argument_list|()
decl_stmt|;
if|if
condition|(
name|entries
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|odataFeed
return|;
block|}
name|List
argument_list|<
name|ODataEntry
argument_list|>
name|copyEntries
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|copyEntries
operator|.
name|addAll
argument_list|(
name|entries
argument_list|)
expr_stmt|;
for|for
control|(
name|ODataEntry
name|entry
range|:
name|copyEntries
control|)
block|{
if|if
condition|(
name|resultIndex
operator|.
name|contains
argument_list|(
name|hash
argument_list|(
name|entry
argument_list|)
argument_list|)
condition|)
block|{
name|entries
operator|.
name|remove
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|odataFeed
return|;
block|}
DECL|method|index (ODataFeed odataFeed)
specifier|private
name|void
name|index
parameter_list|(
name|ODataFeed
name|odataFeed
parameter_list|)
block|{
for|for
control|(
name|ODataEntry
name|entry
range|:
name|odataFeed
operator|.
name|getEntries
argument_list|()
control|)
block|{
name|resultIndex
operator|.
name|add
argument_list|(
name|hash
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Index the results      */
DECL|method|index (Object result)
specifier|public
name|void
name|index
parameter_list|(
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|instanceof
name|ODataFeed
condition|)
block|{
name|index
argument_list|(
operator|(
name|ODataFeed
operator|)
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|result
operator|instanceof
name|Iterable
condition|)
block|{
name|index
argument_list|(
operator|(
name|Iterable
argument_list|<
name|?
argument_list|>
operator|)
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|indexDefault
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|filterResponse (Object response)
specifier|public
name|Object
name|filterResponse
parameter_list|(
name|Object
name|response
parameter_list|)
block|{
if|if
condition|(
name|response
operator|instanceof
name|ODataFeed
condition|)
block|{
name|response
operator|=
name|filter
argument_list|(
operator|(
name|ODataFeed
operator|)
name|response
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|response
operator|instanceof
name|Iterable
condition|)
block|{
name|response
operator|=
name|filter
argument_list|(
operator|(
name|Iterable
argument_list|<
name|Object
argument_list|>
operator|)
name|response
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|response
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|int
name|size
init|=
name|Array
operator|.
name|getLength
argument_list|(
name|response
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
name|add
argument_list|(
name|Array
operator|.
name|get
argument_list|(
name|response
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|response
operator|=
name|filter
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|=
name|filter
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
block|}
end_class

end_unit
