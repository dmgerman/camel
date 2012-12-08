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
name|io
operator|.
name|FileFilter
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * File filter using {@link AntPathMatcher}.  *<p/>  * Exclude take precedence over includes. If a file match both exclude and include it will be regarded as excluded.  */
end_comment

begin_class
DECL|class|AntPathMatcherFileFilter
specifier|public
class|class
name|AntPathMatcherFileFilter
implements|implements
name|FileFilter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AntPathMatcherFileFilter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|matcher
specifier|private
name|AntPathMatcher
name|matcher
init|=
operator|new
name|AntPathMatcher
argument_list|()
decl_stmt|;
DECL|field|excludes
specifier|private
name|String
index|[]
name|excludes
decl_stmt|;
DECL|field|includes
specifier|private
name|String
index|[]
name|includes
decl_stmt|;
DECL|field|caseSensitive
specifier|private
name|boolean
name|caseSensitive
init|=
literal|true
decl_stmt|;
DECL|method|accept (File pathname)
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|pathname
parameter_list|)
block|{
return|return
name|acceptPathName
argument_list|(
name|pathname
operator|.
name|getPath
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Accepts the given file by the path name      *      * @param path the path      * @return<tt>true</tt> if accepted,<tt>false</tt> if not      */
DECL|method|acceptPathName (String path)
specifier|public
name|boolean
name|acceptPathName
parameter_list|(
name|String
name|path
parameter_list|)
block|{
comment|// must use single / as path separators
name|path
operator|=
name|path
operator|.
name|replace
argument_list|(
name|File
operator|.
name|separatorChar
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Filtering file: {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
comment|// excludes take precedence
if|if
condition|(
name|excludes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|exclude
range|:
name|excludes
control|)
block|{
if|if
condition|(
name|matcher
operator|.
name|match
argument_list|(
name|exclude
argument_list|,
name|path
argument_list|,
name|caseSensitive
argument_list|)
condition|)
block|{
comment|// something to exclude so we cant accept it
name|LOG
operator|.
name|trace
argument_list|(
literal|"File is excluded: {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
if|if
condition|(
name|includes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|include
range|:
name|includes
control|)
block|{
if|if
condition|(
name|matcher
operator|.
name|match
argument_list|(
name|include
argument_list|,
name|path
argument_list|,
name|caseSensitive
argument_list|)
condition|)
block|{
comment|// something to include so we accept it
name|LOG
operator|.
name|trace
argument_list|(
literal|"File is included: {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
if|if
condition|(
name|excludes
operator|!=
literal|null
operator|&&
name|includes
operator|==
literal|null
condition|)
block|{
comment|// if the user specified excludes but no includes, presumably we should include by default
return|return
literal|true
return|;
block|}
comment|// nothing to include so we can't accept it
return|return
literal|false
return|;
block|}
comment|/**      *      * @return<tt>true</tt> if case sensitive pattern matching is on,      *<tt>false</tt> if case sensitive pattern matching is off.      */
DECL|method|isCaseSensitive ()
specifier|public
name|boolean
name|isCaseSensitive
parameter_list|()
block|{
return|return
name|caseSensitive
return|;
block|}
comment|/**      * Sets Whether or not pattern matching should be case sensitive      *<p/>      * Is by default turned on<tt>true</tt>.      * @param caseSensitive<tt>false</tt> to disable case sensitive pattern matching      */
DECL|method|setCaseSensitive (boolean caseSensitive)
specifier|public
name|void
name|setCaseSensitive
parameter_list|(
name|boolean
name|caseSensitive
parameter_list|)
block|{
name|this
operator|.
name|caseSensitive
operator|=
name|caseSensitive
expr_stmt|;
block|}
DECL|method|getExcludes ()
specifier|public
name|String
index|[]
name|getExcludes
parameter_list|()
block|{
return|return
name|excludes
return|;
block|}
DECL|method|setExcludes (String[] excludes)
specifier|public
name|void
name|setExcludes
parameter_list|(
name|String
index|[]
name|excludes
parameter_list|)
block|{
name|this
operator|.
name|excludes
operator|=
name|excludes
expr_stmt|;
block|}
DECL|method|getIncludes ()
specifier|public
name|String
index|[]
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
DECL|method|setIncludes (String[] includes)
specifier|public
name|void
name|setIncludes
parameter_list|(
name|String
index|[]
name|includes
parameter_list|)
block|{
name|this
operator|.
name|includes
operator|=
name|includes
expr_stmt|;
block|}
comment|/**      * Sets excludes using a single string where each element can be separated with comma      */
DECL|method|setExcludes (String excludes)
specifier|public
name|void
name|setExcludes
parameter_list|(
name|String
name|excludes
parameter_list|)
block|{
name|setExcludes
argument_list|(
name|excludes
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets includes using a single string where each element can be separated with comma      */
DECL|method|setIncludes (String includes)
specifier|public
name|void
name|setIncludes
parameter_list|(
name|String
name|includes
parameter_list|)
block|{
name|setIncludes
argument_list|(
name|includes
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

