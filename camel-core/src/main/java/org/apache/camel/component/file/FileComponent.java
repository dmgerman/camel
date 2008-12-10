begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|Iterator
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
name|CamelContext
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
name|impl
operator|.
name|DefaultComponent
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNotEmpty
import|;
end_import

begin_comment
comment|/**  * The<a href="http://activemq.apache.org/camel/file.html">File Component</a>  * for working with file systems  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|FileComponent
specifier|public
class|class
name|FileComponent
extends|extends
name|DefaultComponent
block|{
comment|/**      * Header key holding the value: the fixed filename to use for producing files.      */
DECL|field|HEADER_FILE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_FILE_NAME
init|=
literal|"org.apache.camel.file.name"
decl_stmt|;
comment|/**      * Header key holding the value: absolute filepath for the actual file produced (by file producer).      * Value is set automatically by Camel      */
DECL|field|HEADER_FILE_NAME_PRODUCED
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_FILE_NAME_PRODUCED
init|=
literal|"org.apache.camel.file.name.produced"
decl_stmt|;
comment|/**      * Header key holding the value: current index of total in the batch being consumed      */
DECL|field|HEADER_FILE_BATCH_INDEX
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_FILE_BATCH_INDEX
init|=
literal|"org.apache.camel.file.index"
decl_stmt|;
comment|/**      * Header key holding the value: total in the batch being consumed      */
DECL|field|HEADER_FILE_BATCH_TOTAL
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_FILE_BATCH_TOTAL
init|=
literal|"org.apache.camel.file.total"
decl_stmt|;
DECL|method|FileComponent ()
specifier|public
name|FileComponent
parameter_list|()
block|{     }
DECL|method|FileComponent (CamelContext context)
specifier|public
name|FileComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|FileEndpoint
name|result
init|=
operator|new
name|FileEndpoint
argument_list|(
name|file
argument_list|,
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
comment|// sort by using file language
name|String
name|sortBy
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"sortBy"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|isNotEmpty
argument_list|(
name|sortBy
argument_list|)
operator|&&
operator|!
name|isReferenceParameter
argument_list|(
name|sortBy
argument_list|)
condition|)
block|{
comment|// we support nested sort groups so they should be chained
name|String
index|[]
name|groups
init|=
name|sortBy
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|groups
argument_list|)
decl_stmt|;
name|Comparator
argument_list|<
name|FileExchange
argument_list|>
name|comparator
init|=
name|createSortByComparator
argument_list|(
name|it
argument_list|)
decl_stmt|;
name|result
operator|.
name|setSortBy
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|result
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|createSortByComparator (Iterator<String> it)
specifier|private
name|Comparator
argument_list|<
name|FileExchange
argument_list|>
name|createSortByComparator
parameter_list|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
parameter_list|)
block|{
if|if
condition|(
operator|!
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|group
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|boolean
name|reverse
init|=
name|group
operator|.
name|startsWith
argument_list|(
literal|"reverse:"
argument_list|)
decl_stmt|;
name|String
name|reminder
init|=
name|reverse
condition|?
name|ifStartsWithReturnRemainder
argument_list|(
literal|"reverse:"
argument_list|,
name|group
argument_list|)
else|:
name|group
decl_stmt|;
name|boolean
name|ignoreCase
init|=
name|reminder
operator|.
name|startsWith
argument_list|(
literal|"ignoreCase:"
argument_list|)
decl_stmt|;
name|reminder
operator|=
name|ignoreCase
condition|?
name|ifStartsWithReturnRemainder
argument_list|(
literal|"ignoreCase:"
argument_list|,
name|reminder
argument_list|)
else|:
name|reminder
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|reminder
argument_list|,
literal|"sortBy expression"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// recursive add nested sorters
return|return
name|DefaultFileSorter
operator|.
name|sortByFileLanguage
argument_list|(
name|reminder
argument_list|,
name|reverse
argument_list|,
name|ignoreCase
argument_list|,
name|createSortByComparator
argument_list|(
name|it
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

