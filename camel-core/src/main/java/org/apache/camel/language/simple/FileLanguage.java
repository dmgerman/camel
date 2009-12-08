begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
package|;
end_package

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

begin_comment
comment|/**  * File language is an extension to Simple language to add file specific expressions.  *  * Examples of supported file expressions are:  *<ul>  *<li><tt>file:name</tt> to access the file name (is relative, see note below))</li>  *<li><tt>file:name.noext</tt> to access the file name with no extension</li>  *<li><tt>file:ext</tt> to access the file extension</li>  *<li><tt>file:onlyname</tt> to access the file name (no paths)</li>  *<li><tt>file:onlyname.noext</tt> to access the file name (no paths) with no extension</li>  *<li><tt>file:parent</tt> to access the parent file name</li>  *<li><tt>file:path</tt> to access the file path name</li>  *<li><tt>file:absolute</tt> is the file regarded as absolute or relative</li>  *<li><tt>file:absolute.path</tt> to access the absolute file path name</li>  *<li><tt>file:length</tt> to access the file length as a Long type</li>  *<li><tt>file:modified</tt> to access the file last modified as a Date type</li>  *<li><tt>date:&lt;command&gt;:&lt;pattern&gt;</tt> for date formatting using the {@link java.text.SimpleDateFormat} patterns.  *     Additional Supported commands are:<tt>file</tt> for the last modified timestamp of the file.  *     All the commands from {@link SimpleLanguage} is also available.  *</li>  *</ul>  * The<b>relative</b> file is the filename with the starting directory clipped, as opposed to<b>path</b> that will  * return the full path including the starting directory.  *<br/>  * The<b>only</b> file is the filename only with all paths clipped.  *<br/>   * All the simple expression is also available so you can eg use<tt>${in.header.foo}</tt> to access the foo header.  *  * @see org.apache.camel.language.simple.SimpleLanguage  * @see org.apache.camel.language.bean.BeanLanguage  * @deprecated file language is now included as standard in simple language, will be removed in Camel 2.4  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|FileLanguage
specifier|public
class|class
name|FileLanguage
extends|extends
name|SimpleLanguageSupport
block|{
DECL|field|SIMPLE
specifier|private
specifier|static
specifier|final
name|SimpleLanguage
name|SIMPLE
init|=
operator|new
name|SimpleLanguage
argument_list|()
decl_stmt|;
DECL|method|file (String expression)
specifier|public
specifier|static
name|Expression
name|file
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|SimpleLanguage
operator|.
name|simple
argument_list|(
name|expression
argument_list|)
return|;
block|}
DECL|method|createSimpleExpression (String expression, boolean strict)
specifier|protected
name|Expression
name|createSimpleExpression
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
return|return
name|SIMPLE
operator|.
name|createSimpleExpression
argument_list|(
name|expression
argument_list|,
name|strict
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

