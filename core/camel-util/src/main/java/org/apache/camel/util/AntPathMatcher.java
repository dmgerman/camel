begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_comment
comment|/**  * PathMatcher implementation for Ant-style path patterns. Examples are provided below.  *<p>  * Part of this mapping code has been kindly borrowed from<a href="http://ant.apache.org">Apache Ant</a>  * and<a href="http://springframework.org">Spring Framework</a>.  *<p>  * The mapping matches URLs using the following rules:<br>  *<ul>  *<li>? matches one character</li>  *<li>* matches zero or more characters</li>  *<li>** matches zero or more 'directories' in a path</li>  *</ul>  *<p>  * Some examples:<br>  *<ul>  *<li><code>com/t?st.jsp</code> - matches<code>com/test.jsp</code> but also  *<code>com/tast.jsp</code> or<code>com/txst.jsp</code>  *</li>  *<li><code>com/*.jsp</code> - matches all<code>.jsp</code> files in the  *<code>com</code> directory  *</li>  *<li><code>com/&#42;&#42;/test.jsp</code> - matches all<code>test.jsp</code>  *       files underneath the<code>com</code> path  *</li>  *<li><code>org/springframework/&#42;&#42;/*.jsp</code> - matches all  *<code>.jsp</code> files underneath the<code>org/springframework</code> path  *</li>  *<li><code>org/&#42;&#42;/servlet/bla.jsp</code> - matches  *<code>org/springframework/servlet/bla.jsp</code> but also  *<code>org/springframework/testing/servlet/bla.jsp</code> and  *<code>org/servlet/bla.jsp</code>  *</li>  *</ul>  */
end_comment

begin_class
DECL|class|AntPathMatcher
specifier|public
class|class
name|AntPathMatcher
block|{
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|AntPathMatcher
name|INSTANCE
init|=
operator|new
name|AntPathMatcher
argument_list|()
decl_stmt|;
comment|/** Default path separator: "/" */
DECL|field|DEFAULT_PATH_SEPARATOR
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PATH_SEPARATOR
init|=
literal|"/"
decl_stmt|;
DECL|field|pathSeparator
specifier|private
name|String
name|pathSeparator
init|=
name|DEFAULT_PATH_SEPARATOR
decl_stmt|;
comment|/**      * Set the path separator to use for pattern parsing. Default is "/", as in      * Ant.      */
DECL|method|setPathSeparator (String pathSeparator)
specifier|public
name|void
name|setPathSeparator
parameter_list|(
name|String
name|pathSeparator
parameter_list|)
block|{
name|this
operator|.
name|pathSeparator
operator|=
name|pathSeparator
operator|!=
literal|null
condition|?
name|pathSeparator
else|:
name|DEFAULT_PATH_SEPARATOR
expr_stmt|;
block|}
DECL|method|isPattern (String path)
specifier|public
name|boolean
name|isPattern
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|path
operator|.
name|indexOf
argument_list|(
literal|'*'
argument_list|)
operator|!=
operator|-
literal|1
operator|||
name|path
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|!=
operator|-
literal|1
return|;
block|}
DECL|method|match (String pattern, String path)
specifier|public
name|boolean
name|match
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|path
parameter_list|)
block|{
return|return
name|match
argument_list|(
name|pattern
argument_list|,
name|path
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|matchStart (String pattern, String path)
specifier|public
name|boolean
name|matchStart
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|path
parameter_list|)
block|{
return|return
name|matchStart
argument_list|(
name|pattern
argument_list|,
name|path
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|match (String pattern, String path, boolean isCaseSensitive)
specifier|public
name|boolean
name|match
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|path
parameter_list|,
name|boolean
name|isCaseSensitive
parameter_list|)
block|{
return|return
name|doMatch
argument_list|(
name|pattern
argument_list|,
name|path
argument_list|,
literal|true
argument_list|,
name|isCaseSensitive
argument_list|)
return|;
block|}
DECL|method|matchStart (String pattern, String path, boolean isCaseSensitive)
specifier|public
name|boolean
name|matchStart
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|path
parameter_list|,
name|boolean
name|isCaseSensitive
parameter_list|)
block|{
return|return
name|doMatch
argument_list|(
name|pattern
argument_list|,
name|path
argument_list|,
literal|false
argument_list|,
name|isCaseSensitive
argument_list|)
return|;
block|}
comment|/**      * Actually match the given<code>path</code> against the given      *<code>pattern</code>.      *       * @param pattern the pattern to match against      * @param path the path String to test      * @param fullMatch whether a full pattern match is required (else a pattern      *            match as far as the given base path goes is sufficient)      * @param isCaseSensitive Whether or not matching should be performed      *                        case sensitively.      * @return<code>true</code> if the supplied<code>path</code> matched,      *<code>false</code> if it didn't      */
DECL|method|doMatch (String pattern, String path, boolean fullMatch, boolean isCaseSensitive)
specifier|protected
name|boolean
name|doMatch
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|path
parameter_list|,
name|boolean
name|fullMatch
parameter_list|,
name|boolean
name|isCaseSensitive
parameter_list|)
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|this
operator|.
name|pathSeparator
argument_list|)
operator|!=
name|pattern
operator|.
name|startsWith
argument_list|(
name|this
operator|.
name|pathSeparator
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
index|[]
name|pattDirs
init|=
name|tokenizeToStringArray
argument_list|(
name|pattern
argument_list|,
name|this
operator|.
name|pathSeparator
argument_list|)
decl_stmt|;
name|String
index|[]
name|pathDirs
init|=
name|tokenizeToStringArray
argument_list|(
name|path
argument_list|,
name|this
operator|.
name|pathSeparator
argument_list|)
decl_stmt|;
name|int
name|pattIdxStart
init|=
literal|0
decl_stmt|;
name|int
name|pattIdxEnd
init|=
name|pattDirs
operator|.
name|length
operator|-
literal|1
decl_stmt|;
name|int
name|pathIdxStart
init|=
literal|0
decl_stmt|;
name|int
name|pathIdxEnd
init|=
name|pathDirs
operator|.
name|length
operator|-
literal|1
decl_stmt|;
comment|// Match all elements up to the first **
while|while
condition|(
name|pattIdxStart
operator|<=
name|pattIdxEnd
operator|&&
name|pathIdxStart
operator|<=
name|pathIdxEnd
condition|)
block|{
name|String
name|patDir
init|=
name|pattDirs
index|[
name|pattIdxStart
index|]
decl_stmt|;
if|if
condition|(
literal|"**"
operator|.
name|equals
argument_list|(
name|patDir
argument_list|)
condition|)
block|{
break|break;
block|}
if|if
condition|(
operator|!
name|matchStrings
argument_list|(
name|patDir
argument_list|,
name|pathDirs
index|[
name|pathIdxStart
index|]
argument_list|,
name|isCaseSensitive
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|pattIdxStart
operator|++
expr_stmt|;
name|pathIdxStart
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|pathIdxStart
operator|>
name|pathIdxEnd
condition|)
block|{
comment|// Path is exhausted, only match if rest of pattern is * or **'s
if|if
condition|(
name|pattIdxStart
operator|>
name|pattIdxEnd
condition|)
block|{
return|return
name|pattern
operator|.
name|endsWith
argument_list|(
name|this
operator|.
name|pathSeparator
argument_list|)
condition|?
name|path
operator|.
name|endsWith
argument_list|(
name|this
operator|.
name|pathSeparator
argument_list|)
else|:
operator|!
name|path
operator|.
name|endsWith
argument_list|(
name|this
operator|.
name|pathSeparator
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|fullMatch
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|pattIdxStart
operator|==
name|pattIdxEnd
operator|&&
name|pattDirs
index|[
name|pattIdxStart
index|]
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
operator|&&
name|path
operator|.
name|endsWith
argument_list|(
name|this
operator|.
name|pathSeparator
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|int
name|i
init|=
name|pattIdxStart
init|;
name|i
operator|<=
name|pattIdxEnd
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|pattDirs
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"**"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|pattIdxStart
operator|>
name|pattIdxEnd
condition|)
block|{
comment|// String not exhausted, but pattern is. Failure.
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|fullMatch
operator|&&
literal|"**"
operator|.
name|equals
argument_list|(
name|pattDirs
index|[
name|pattIdxStart
index|]
argument_list|)
condition|)
block|{
comment|// Path start definitely matches due to "**" part in pattern.
return|return
literal|true
return|;
block|}
comment|// up to last '**'
while|while
condition|(
name|pattIdxStart
operator|<=
name|pattIdxEnd
operator|&&
name|pathIdxStart
operator|<=
name|pathIdxEnd
condition|)
block|{
name|String
name|patDir
init|=
name|pattDirs
index|[
name|pattIdxEnd
index|]
decl_stmt|;
if|if
condition|(
name|patDir
operator|.
name|equals
argument_list|(
literal|"**"
argument_list|)
condition|)
block|{
break|break;
block|}
if|if
condition|(
operator|!
name|matchStrings
argument_list|(
name|patDir
argument_list|,
name|pathDirs
index|[
name|pathIdxEnd
index|]
argument_list|,
name|isCaseSensitive
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|pattIdxEnd
operator|--
expr_stmt|;
name|pathIdxEnd
operator|--
expr_stmt|;
block|}
if|if
condition|(
name|pathIdxStart
operator|>
name|pathIdxEnd
condition|)
block|{
comment|// String is exhausted
for|for
control|(
name|int
name|i
init|=
name|pattIdxStart
init|;
name|i
operator|<=
name|pattIdxEnd
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|pattDirs
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"**"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
while|while
condition|(
name|pattIdxStart
operator|!=
name|pattIdxEnd
operator|&&
name|pathIdxStart
operator|<=
name|pathIdxEnd
condition|)
block|{
name|int
name|patIdxTmp
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|pattIdxStart
operator|+
literal|1
init|;
name|i
operator|<=
name|pattIdxEnd
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|pattDirs
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"**"
argument_list|)
condition|)
block|{
name|patIdxTmp
operator|=
name|i
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|patIdxTmp
operator|==
name|pattIdxStart
operator|+
literal|1
condition|)
block|{
comment|// '**/**' situation, so skip one
name|pattIdxStart
operator|++
expr_stmt|;
continue|continue;
block|}
comment|// Find the pattern between padIdxStart& padIdxTmp in str between
comment|// strIdxStart& strIdxEnd
name|int
name|patLength
init|=
name|patIdxTmp
operator|-
name|pattIdxStart
operator|-
literal|1
decl_stmt|;
name|int
name|strLength
init|=
name|pathIdxEnd
operator|-
name|pathIdxStart
operator|+
literal|1
decl_stmt|;
name|int
name|foundIdx
init|=
operator|-
literal|1
decl_stmt|;
name|strLoop
label|:
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
name|strLength
operator|-
name|patLength
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|patLength
condition|;
name|j
operator|++
control|)
block|{
name|String
name|subPat
init|=
name|pattDirs
index|[
name|pattIdxStart
operator|+
name|j
operator|+
literal|1
index|]
decl_stmt|;
name|String
name|subStr
init|=
name|pathDirs
index|[
name|pathIdxStart
operator|+
name|i
operator|+
name|j
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|matchStrings
argument_list|(
name|subPat
argument_list|,
name|subStr
argument_list|,
name|isCaseSensitive
argument_list|)
condition|)
block|{
continue|continue
name|strLoop
continue|;
block|}
block|}
name|foundIdx
operator|=
name|pathIdxStart
operator|+
name|i
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|foundIdx
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|false
return|;
block|}
name|pattIdxStart
operator|=
name|patIdxTmp
expr_stmt|;
name|pathIdxStart
operator|=
name|foundIdx
operator|+
name|patLength
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
name|pattIdxStart
init|;
name|i
operator|<=
name|pattIdxEnd
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|pattDirs
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"**"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Tests whether or not a string matches against a pattern. The pattern may      * contain two special characters:<br>      * '*' means zero or more characters<br>      * '?' means one and only one character      *       * @param pattern pattern to match against. Must not be<code>null</code>.      * @param str string which must be matched against the pattern. Must not be      *<code>null</code>.      * @param caseSensitive Whether or not matching should be performed      *                      case sensitively.      * @return<code>true</code> if the string matches against the pattern, or      *<code>false</code> otherwise.      */
DECL|method|matchStrings (String pattern, String str, boolean caseSensitive)
specifier|private
name|boolean
name|matchStrings
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|str
parameter_list|,
name|boolean
name|caseSensitive
parameter_list|)
block|{
name|char
index|[]
name|patArr
init|=
name|pattern
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|char
index|[]
name|strArr
init|=
name|str
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|int
name|patIdxStart
init|=
literal|0
decl_stmt|;
name|int
name|patIdxEnd
init|=
name|patArr
operator|.
name|length
operator|-
literal|1
decl_stmt|;
name|int
name|strIdxStart
init|=
literal|0
decl_stmt|;
name|int
name|strIdxEnd
init|=
name|strArr
operator|.
name|length
operator|-
literal|1
decl_stmt|;
name|char
name|ch
decl_stmt|;
name|boolean
name|containsStar
init|=
literal|false
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|patArr
control|)
block|{
if|if
condition|(
name|c
operator|==
literal|'*'
condition|)
block|{
name|containsStar
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|containsStar
condition|)
block|{
comment|// No '*'s, so we make a shortcut
if|if
condition|(
name|patIdxEnd
operator|!=
name|strIdxEnd
condition|)
block|{
return|return
literal|false
return|;
comment|// Pattern and string do not have the same size
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
name|patIdxEnd
condition|;
name|i
operator|++
control|)
block|{
name|ch
operator|=
name|patArr
index|[
name|i
index|]
expr_stmt|;
if|if
condition|(
name|ch
operator|!=
literal|'?'
condition|)
block|{
if|if
condition|(
name|different
argument_list|(
name|caseSensitive
argument_list|,
name|ch
argument_list|,
name|strArr
index|[
name|i
index|]
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
comment|// Character mismatch
block|}
block|}
block|}
return|return
literal|true
return|;
comment|// String matches against pattern
block|}
if|if
condition|(
name|patIdxEnd
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
comment|// Pattern contains only '*', which matches anything
block|}
comment|// Process characters before first star
while|while
condition|(
operator|(
name|ch
operator|=
name|patArr
index|[
name|patIdxStart
index|]
operator|)
operator|!=
literal|'*'
operator|&&
name|strIdxStart
operator|<=
name|strIdxEnd
condition|)
block|{
if|if
condition|(
name|ch
operator|!=
literal|'?'
condition|)
block|{
if|if
condition|(
name|different
argument_list|(
name|caseSensitive
argument_list|,
name|ch
argument_list|,
name|strArr
index|[
name|strIdxStart
index|]
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
comment|// Character mismatch
block|}
block|}
name|patIdxStart
operator|++
expr_stmt|;
name|strIdxStart
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|strIdxStart
operator|>
name|strIdxEnd
condition|)
block|{
comment|// All characters in the string are used. Check if only '*'s are
comment|// left in the pattern. If so, we succeeded. Otherwise failure.
for|for
control|(
name|int
name|i
init|=
name|patIdxStart
init|;
name|i
operator|<=
name|patIdxEnd
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|patArr
index|[
name|i
index|]
operator|!=
literal|'*'
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|// Process characters after last star
while|while
condition|(
operator|(
name|ch
operator|=
name|patArr
index|[
name|patIdxEnd
index|]
operator|)
operator|!=
literal|'*'
operator|&&
name|strIdxStart
operator|<=
name|strIdxEnd
condition|)
block|{
if|if
condition|(
name|ch
operator|!=
literal|'?'
condition|)
block|{
if|if
condition|(
name|different
argument_list|(
name|caseSensitive
argument_list|,
name|ch
argument_list|,
name|strArr
index|[
name|strIdxEnd
index|]
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
comment|// Character mismatch
block|}
block|}
name|patIdxEnd
operator|--
expr_stmt|;
name|strIdxEnd
operator|--
expr_stmt|;
block|}
if|if
condition|(
name|strIdxStart
operator|>
name|strIdxEnd
condition|)
block|{
comment|// All characters in the string are used. Check if only '*'s are
comment|// left in the pattern. If so, we succeeded. Otherwise failure.
for|for
control|(
name|int
name|i
init|=
name|patIdxStart
init|;
name|i
operator|<=
name|patIdxEnd
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|patArr
index|[
name|i
index|]
operator|!=
literal|'*'
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|// process pattern between stars. padIdxStart and patIdxEnd point
comment|// always to a '*'.
while|while
condition|(
name|patIdxStart
operator|!=
name|patIdxEnd
operator|&&
name|strIdxStart
operator|<=
name|strIdxEnd
condition|)
block|{
name|int
name|patIdxTmp
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|patIdxStart
operator|+
literal|1
init|;
name|i
operator|<=
name|patIdxEnd
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|patArr
index|[
name|i
index|]
operator|==
literal|'*'
condition|)
block|{
name|patIdxTmp
operator|=
name|i
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|patIdxTmp
operator|==
name|patIdxStart
operator|+
literal|1
condition|)
block|{
comment|// Two stars next to each other, skip the first one.
name|patIdxStart
operator|++
expr_stmt|;
continue|continue;
block|}
comment|// Find the pattern between padIdxStart& padIdxTmp in str between
comment|// strIdxStart& strIdxEnd
name|int
name|patLength
init|=
name|patIdxTmp
operator|-
name|patIdxStart
operator|-
literal|1
decl_stmt|;
name|int
name|strLength
init|=
name|strIdxEnd
operator|-
name|strIdxStart
operator|+
literal|1
decl_stmt|;
name|int
name|foundIdx
init|=
operator|-
literal|1
decl_stmt|;
name|strLoop
label|:
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
name|strLength
operator|-
name|patLength
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|patLength
condition|;
name|j
operator|++
control|)
block|{
name|ch
operator|=
name|patArr
index|[
name|patIdxStart
operator|+
name|j
operator|+
literal|1
index|]
expr_stmt|;
if|if
condition|(
name|ch
operator|!=
literal|'?'
condition|)
block|{
if|if
condition|(
name|different
argument_list|(
name|caseSensitive
argument_list|,
name|ch
argument_list|,
name|strArr
index|[
name|strIdxStart
operator|+
name|i
operator|+
name|j
index|]
argument_list|)
condition|)
block|{
continue|continue
name|strLoop
continue|;
block|}
block|}
block|}
name|foundIdx
operator|=
name|strIdxStart
operator|+
name|i
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|foundIdx
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|false
return|;
block|}
name|patIdxStart
operator|=
name|patIdxTmp
expr_stmt|;
name|strIdxStart
operator|=
name|foundIdx
operator|+
name|patLength
expr_stmt|;
block|}
comment|// All characters in the string are used. Check if only '*'s are left
comment|// in the pattern. If so, we succeeded. Otherwise failure.
for|for
control|(
name|int
name|i
init|=
name|patIdxStart
init|;
name|i
operator|<=
name|patIdxEnd
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|patArr
index|[
name|i
index|]
operator|!=
literal|'*'
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Given a pattern and a full path, determine the pattern-mapped part.      *<p>      * For example:      *<ul>      *<li>'<code>/docs/cvs/commit.html</code>' and '      *<code>/docs/cvs/commit.html</code> -> ''</li>      *<li>'<code>/docs/*</code>' and '<code>/docs/cvs/commit</code> -> '      *<code>cvs/commit</code>'</li>      *<li>'<code>/docs/cvs/*.html</code>' and '      *<code>/docs/cvs/commit.html</code> -> '<code>commit.html</code>'</li>      *<li>'<code>/docs/**</code>' and '<code>/docs/cvs/commit</code> -> '      *<code>cvs/commit</code>'</li>      *<li>'<code>/docs/**\/*.html</code>' and '      *<code>/docs/cvs/commit.html</code> -> '<code>cvs/commit.html</code>'</li>      *<li>'<code>/*.html</code>' and '<code>/docs/cvs/commit.html</code> -> '      *<code>docs/cvs/commit.html</code>'</li>      *<li>'<code>*.html</code>' and '<code>/docs/cvs/commit.html</code> -> '      *<code>/docs/cvs/commit.html</code>'</li>      *<li>'<code>*</code>' and '<code>/docs/cvs/commit.html</code> -> '      *<code>/docs/cvs/commit.html</code>'</li>      *</ul>      *<p>      * Assumes that {@link #match} returns<code>true</code> for '      *<code>pattern</code>' and '<code>path</code>', but does      *<strong>not</strong> enforce this.      */
DECL|method|extractPathWithinPattern (String pattern, String path)
specifier|public
name|String
name|extractPathWithinPattern
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|String
index|[]
name|patternParts
init|=
name|tokenizeToStringArray
argument_list|(
name|pattern
argument_list|,
name|this
operator|.
name|pathSeparator
argument_list|)
decl_stmt|;
name|String
index|[]
name|pathParts
init|=
name|tokenizeToStringArray
argument_list|(
name|path
argument_list|,
name|this
operator|.
name|pathSeparator
argument_list|)
decl_stmt|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// Add any path parts that have a wildcarded pattern part.
name|int
name|puts
init|=
literal|0
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
name|patternParts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|patternPart
init|=
name|patternParts
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
operator|(
name|patternPart
operator|.
name|indexOf
argument_list|(
literal|'*'
argument_list|)
operator|>
operator|-
literal|1
operator|||
name|patternPart
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|>
operator|-
literal|1
operator|)
operator|&&
name|pathParts
operator|.
name|length
operator|>=
name|i
operator|+
literal|1
condition|)
block|{
if|if
condition|(
name|puts
operator|>
literal|0
operator|||
operator|(
name|i
operator|==
literal|0
operator|&&
operator|!
name|pattern
operator|.
name|startsWith
argument_list|(
name|this
operator|.
name|pathSeparator
argument_list|)
operator|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|this
operator|.
name|pathSeparator
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|pathParts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|puts
operator|++
expr_stmt|;
block|}
block|}
comment|// Append any trailing path parts.
for|for
control|(
name|int
name|i
init|=
name|patternParts
operator|.
name|length
init|;
name|i
operator|<
name|pathParts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|puts
operator|>
literal|0
operator|||
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|this
operator|.
name|pathSeparator
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|pathParts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Tokenize the given String into a String array via a StringTokenizer.      * Trims tokens and omits empty tokens.      *<p>      * The given delimiters string is supposed to consist of any number of      * delimiter characters. Each of those characters can be used to separate      * tokens. A delimiter is always a single character; for multi-character      * delimiters, consider using<code>delimitedListToStringArray</code>      *       * @param str the String to tokenize      * @param delimiters the delimiter characters, assembled as String (each of      *            those characters is individually considered as delimiter).      * @return an array of the tokens      * @see java.util.StringTokenizer      * @see java.lang.String#trim()      */
DECL|method|tokenizeToStringArray (String str, String delimiters)
specifier|public
specifier|static
name|String
index|[]
name|tokenizeToStringArray
parameter_list|(
name|String
name|str
parameter_list|,
name|String
name|delimiters
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|str
argument_list|,
name|delimiters
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|tokens
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|token
init|=
name|st
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|token
operator|=
name|token
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|tokens
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|tokens
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
DECL|method|different (boolean caseSensitive, char ch, char other)
specifier|private
specifier|static
name|boolean
name|different
parameter_list|(
name|boolean
name|caseSensitive
parameter_list|,
name|char
name|ch
parameter_list|,
name|char
name|other
parameter_list|)
block|{
return|return
name|caseSensitive
condition|?
name|ch
operator|!=
name|other
else|:
name|Character
operator|.
name|toUpperCase
argument_list|(
name|ch
argument_list|)
operator|!=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|other
argument_list|)
return|;
block|}
block|}
end_class

end_unit
