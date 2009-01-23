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
name|util
operator|.
name|Comparator
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
name|Expression
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
name|language
operator|.
name|simple
operator|.
name|FileLanguage
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

begin_comment
comment|/**  * Default remote file sorter.  */
end_comment

begin_class
DECL|class|GenericDefaultFileSorter
specifier|public
specifier|final
class|class
name|GenericDefaultFileSorter
block|{
DECL|method|GenericDefaultFileSorter ()
specifier|private
name|GenericDefaultFileSorter
parameter_list|()
block|{     }
comment|/**      * Returns a new sort by name      */
DECL|method|sortByName (final boolean reverse)
specifier|public
specifier|static
name|Comparator
argument_list|<
name|GenericFile
argument_list|>
name|sortByName
parameter_list|(
specifier|final
name|boolean
name|reverse
parameter_list|)
block|{
return|return
operator|new
name|Comparator
argument_list|<
name|GenericFile
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|GenericFile
name|o1
parameter_list|,
name|GenericFile
name|o2
parameter_list|)
block|{
name|int
name|answer
init|=
name|o1
operator|.
name|getFileName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getFileName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|reverse
condition|?
operator|-
literal|1
operator|*
name|answer
else|:
name|answer
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns a new sort by path name      */
DECL|method|sortByPathName (final boolean reverse)
specifier|public
specifier|static
name|Comparator
argument_list|<
name|GenericFile
argument_list|>
name|sortByPathName
parameter_list|(
specifier|final
name|boolean
name|reverse
parameter_list|)
block|{
return|return
operator|new
name|Comparator
argument_list|<
name|GenericFile
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|GenericFile
name|o1
parameter_list|,
name|GenericFile
name|o2
parameter_list|)
block|{
name|int
name|answer
init|=
name|o1
operator|.
name|getParent
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getParent
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|reverse
condition|?
operator|-
literal|1
operator|*
name|answer
else|:
name|answer
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns a new sort by last modified (newest first)      */
DECL|method|sortByLastModified ( final boolean reverse)
specifier|public
specifier|static
name|Comparator
argument_list|<
name|GenericFile
argument_list|>
name|sortByLastModified
parameter_list|(
specifier|final
name|boolean
name|reverse
parameter_list|)
block|{
return|return
operator|new
name|Comparator
argument_list|<
name|GenericFile
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|GenericFile
name|o1
parameter_list|,
name|GenericFile
name|o2
parameter_list|)
block|{
name|long
name|delta
init|=
name|o1
operator|.
name|getLastModified
argument_list|()
operator|-
name|o2
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
if|if
condition|(
name|delta
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|answer
init|=
name|delta
operator|>
literal|0
condition|?
literal|1
else|:
operator|-
literal|1
decl_stmt|;
return|return
name|reverse
condition|?
operator|-
literal|1
operator|*
name|answer
else|:
name|answer
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns a new sort by file size (smallest first)      */
DECL|method|sortBySize (final boolean reverse)
specifier|public
specifier|static
name|Comparator
argument_list|<
name|GenericFile
argument_list|>
name|sortBySize
parameter_list|(
specifier|final
name|boolean
name|reverse
parameter_list|)
block|{
return|return
operator|new
name|Comparator
argument_list|<
name|GenericFile
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|GenericFile
name|o1
parameter_list|,
name|GenericFile
name|o2
parameter_list|)
block|{
name|long
name|delta
init|=
name|o1
operator|.
name|getFileLength
argument_list|()
operator|-
name|o2
operator|.
name|getFileLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|delta
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|answer
init|=
name|delta
operator|>
literal|0
condition|?
literal|1
else|:
operator|-
literal|1
decl_stmt|;
return|return
name|reverse
condition|?
operator|-
literal|1
operator|*
name|answer
else|:
name|answer
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns a new sory by file language expression      *      * @param expression the file language expression      * @param reverse    true to reverse order      * @return the comparator      */
DECL|method|sortByFileLanguage ( final String expression, final boolean reverse)
specifier|public
specifier|static
name|Comparator
argument_list|<
name|GenericFileExchange
argument_list|>
name|sortByFileLanguage
parameter_list|(
specifier|final
name|String
name|expression
parameter_list|,
specifier|final
name|boolean
name|reverse
parameter_list|)
block|{
return|return
name|sortByFileLanguage
argument_list|(
name|expression
argument_list|,
name|reverse
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a new sory by file language expression      *      * @param expression the file language expression      * @param reverse    true to reverse order      * @param ignoreCase ignore case if comparing strings      * @return the comparator      */
DECL|method|sortByFileLanguage ( final String expression, final boolean reverse, final boolean ignoreCase)
specifier|public
specifier|static
name|Comparator
argument_list|<
name|GenericFileExchange
argument_list|>
name|sortByFileLanguage
parameter_list|(
specifier|final
name|String
name|expression
parameter_list|,
specifier|final
name|boolean
name|reverse
parameter_list|,
specifier|final
name|boolean
name|ignoreCase
parameter_list|)
block|{
return|return
name|sortByFileLanguage
argument_list|(
name|expression
argument_list|,
name|reverse
argument_list|,
name|ignoreCase
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a new sort by file language expression      *      * @param expression the file language expression      * @param reverse    true to reverse order      * @param ignoreCase ignore case if comparing strings      * @param nested     nested comparator for sub group sorting, can be null      * @return the comparator      */
DECL|method|sortByFileLanguage ( final String expression, final boolean reverse, final boolean ignoreCase, final Comparator<GenericFileExchange> nested)
specifier|public
specifier|static
name|Comparator
argument_list|<
name|GenericFileExchange
argument_list|>
name|sortByFileLanguage
parameter_list|(
specifier|final
name|String
name|expression
parameter_list|,
specifier|final
name|boolean
name|reverse
parameter_list|,
specifier|final
name|boolean
name|ignoreCase
parameter_list|,
specifier|final
name|Comparator
argument_list|<
name|GenericFileExchange
argument_list|>
name|nested
parameter_list|)
block|{
return|return
operator|new
name|Comparator
argument_list|<
name|GenericFileExchange
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|GenericFileExchange
name|o1
parameter_list|,
name|GenericFileExchange
name|o2
parameter_list|)
block|{
specifier|final
name|Expression
name|exp
init|=
name|FileLanguage
operator|.
name|file
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|Object
name|result1
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|o1
argument_list|)
decl_stmt|;
name|Object
name|result2
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|o2
argument_list|)
decl_stmt|;
name|int
name|answer
init|=
name|ObjectHelper
operator|.
name|compare
argument_list|(
name|result1
argument_list|,
name|result2
argument_list|,
name|ignoreCase
argument_list|)
decl_stmt|;
comment|// if equal then sub sort by nested comparator
if|if
condition|(
name|answer
operator|==
literal|0
operator|&&
name|nested
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|nested
operator|.
name|compare
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
block|}
return|return
name|reverse
condition|?
operator|-
literal|1
operator|*
name|answer
else|:
name|answer
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|expression
operator|+
operator|(
name|nested
operator|!=
literal|null
condition|?
literal|";"
operator|+
name|nested
operator|.
name|toString
argument_list|()
else|:
literal|""
operator|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

