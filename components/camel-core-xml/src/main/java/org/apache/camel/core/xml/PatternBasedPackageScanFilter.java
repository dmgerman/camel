begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
package|;
end_package

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
name|Collection
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|PackageScanFilter
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
name|AntPathMatcher
import|;
end_import

begin_comment
comment|/**  * {@link PatternBasedPackageScanFilter} uses an underlying  * {@link AntPathMatcher} to filter scanned files according to include and  * exclude patterns.  *   * @see AntPathMatcher  */
end_comment

begin_class
DECL|class|PatternBasedPackageScanFilter
specifier|public
class|class
name|PatternBasedPackageScanFilter
implements|implements
name|PackageScanFilter
block|{
DECL|field|matcher
specifier|private
specifier|final
name|AntPathMatcher
name|matcher
init|=
operator|new
name|AntPathMatcher
argument_list|()
decl_stmt|;
DECL|field|excludePatterns
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|excludePatterns
decl_stmt|;
DECL|field|includePatterns
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includePatterns
decl_stmt|;
comment|/**      * add and exclude pattern to the filter. Classes matching this pattern will      * not match the filter       */
DECL|method|addExcludePattern (String excludePattern)
specifier|public
name|void
name|addExcludePattern
parameter_list|(
name|String
name|excludePattern
parameter_list|)
block|{
if|if
condition|(
name|excludePatterns
operator|==
literal|null
condition|)
block|{
name|excludePatterns
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|excludePatterns
operator|.
name|add
argument_list|(
name|excludePattern
argument_list|)
expr_stmt|;
block|}
comment|/**      * add and include pattern to the filter. Classes must match one of supplied      * include patterns to match the filter       */
DECL|method|addIncludePattern (String includePattern)
specifier|public
name|void
name|addIncludePattern
parameter_list|(
name|String
name|includePattern
parameter_list|)
block|{
if|if
condition|(
name|includePatterns
operator|==
literal|null
condition|)
block|{
name|includePatterns
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|includePatterns
operator|.
name|add
argument_list|(
name|includePattern
argument_list|)
expr_stmt|;
block|}
DECL|method|addIncludePatterns (Collection<String> includes)
specifier|public
name|void
name|addIncludePatterns
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|includes
parameter_list|)
block|{
if|if
condition|(
name|includePatterns
operator|==
literal|null
condition|)
block|{
name|includePatterns
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|includePatterns
operator|.
name|addAll
argument_list|(
name|includes
argument_list|)
expr_stmt|;
block|}
DECL|method|addExcludePatterns (Collection<String> excludes)
specifier|public
name|void
name|addExcludePatterns
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|excludes
parameter_list|)
block|{
if|if
condition|(
name|excludePatterns
operator|==
literal|null
condition|)
block|{
name|excludePatterns
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|excludePatterns
operator|.
name|addAll
argument_list|(
name|excludes
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests if a given class matches the patterns in this filter. Patterns are      * specified by {@link AntPathMatcher}      *<p>      * if no include or exclude patterns are set then all classes match.      *<p>      * If the filter contains only include filters, then the candidate class      * must match one of the include patterns to match the filter and return      * true.      *<p>      * If the filter contains only exclude filters, then the filter will return      * true unless the candidate class matches an exclude pattern.      *<p>      * if this contains both include and exclude filters, then the above rules      * apply with excludes taking precedence over includes i.e. an include      * pattern of java.util.* and an exclude pattern of java.util.jar.* will      * include a file only if it is in the util pkg and not in the util.jar      * package.      *       * @return true if candidate class matches according to the above rules      */
DECL|method|matches (Class<?> candidateClass)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|candidateClass
parameter_list|)
block|{
name|String
name|candidate
init|=
name|candidateClass
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|includePatterns
operator|!=
literal|null
operator|||
name|excludePatterns
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|excludePatterns
operator|!=
literal|null
operator|&&
name|excludePatterns
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|matchesAny
argument_list|(
name|excludePatterns
argument_list|,
name|candidate
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|includePatterns
operator|!=
literal|null
operator|&&
name|includePatterns
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|matchesAny
argument_list|(
name|includePatterns
argument_list|,
name|candidate
argument_list|)
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|matchesAny (List<String> patterns, String candidate)
specifier|private
name|boolean
name|matchesAny
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|patterns
parameter_list|,
name|String
name|candidate
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
if|if
condition|(
name|matcher
operator|.
name|match
argument_list|(
name|pattern
argument_list|,
name|candidate
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

