begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_comment
comment|/**  * A ClassLoader delegating to a given OSGi bundle.  *  * @version $Rev: 896324 $, $Date: 2010-01-06 07:05:04 +0100 (Wed, 06 Jan 2010) $  */
end_comment

begin_class
DECL|class|BundleDelegatingClassLoader
specifier|public
class|class
name|BundleDelegatingClassLoader
extends|extends
name|ClassLoader
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|BundleDelegatingClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|bundle
specifier|private
specifier|final
name|Bundle
name|bundle
decl_stmt|;
DECL|field|classLoader
specifier|private
specifier|final
name|ClassLoader
name|classLoader
decl_stmt|;
DECL|method|BundleDelegatingClassLoader (Bundle bundle)
specifier|public
name|BundleDelegatingClassLoader
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|this
argument_list|(
name|bundle
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|BundleDelegatingClassLoader (Bundle bundle, ClassLoader classLoader)
specifier|public
name|BundleDelegatingClassLoader
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|this
operator|.
name|bundle
operator|=
name|bundle
expr_stmt|;
name|this
operator|.
name|classLoader
operator|=
name|classLoader
expr_stmt|;
block|}
DECL|method|findClass (String name)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|findClass
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"FindClass: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|bundle
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|findResource (String name)
specifier|protected
name|URL
name|findResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"FindResource: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
name|URL
name|resource
init|=
name|bundle
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|classLoader
operator|!=
literal|null
operator|&&
name|resource
operator|==
literal|null
condition|)
block|{
name|resource
operator|=
name|classLoader
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|resource
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|findResources (String name)
specifier|protected
name|Enumeration
name|findResources
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"FindResource: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|bundle
operator|.
name|getResources
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|loadClass (String name, boolean resolve)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|resolve
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"LoadClass: "
operator|+
name|name
operator|+
literal|", resolve: "
operator|+
name|resolve
argument_list|)
expr_stmt|;
block|}
name|Class
name|clazz
decl_stmt|;
try|try
block|{
name|clazz
operator|=
name|findClass
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|cnfe
parameter_list|)
block|{
if|if
condition|(
name|classLoader
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|clazz
operator|=
name|classLoader
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
operator|+
literal|" from bundle "
operator|+
name|bundle
operator|.
name|getBundleId
argument_list|()
operator|+
literal|" ("
operator|+
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|")"
argument_list|,
name|cnfe
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
operator|+
literal|" from bundle "
operator|+
name|bundle
operator|.
name|getBundleId
argument_list|()
operator|+
literal|" ("
operator|+
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|")"
argument_list|,
name|cnfe
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|resolve
condition|)
block|{
name|resolveClass
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
return|return
name|clazz
return|;
block|}
DECL|method|getBundle ()
specifier|public
name|Bundle
name|getBundle
parameter_list|()
block|{
return|return
name|bundle
return|;
block|}
block|}
end_class

end_unit

