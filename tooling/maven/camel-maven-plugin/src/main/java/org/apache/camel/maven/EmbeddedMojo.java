begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|net
operator|.
name|URLClassLoader
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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|mojo
operator|.
name|exec
operator|.
name|AbstractExecMojo
import|;
end_import

begin_comment
comment|/**  * Runs a CamelContext using any Spring XML configuration files found in  *<code>META-INF/spring/*.xml</code> and<code>camel-*.xml</code>  * and starting up the context; then generating  * the DOT file before closing the context down.  *  * @goal embedded  * @requiresDependencyResolution compile+runtime  * @execute phase="prepare-package"  */
end_comment

begin_class
DECL|class|EmbeddedMojo
specifier|public
class|class
name|EmbeddedMojo
extends|extends
name|AbstractExecMojo
block|{
comment|/**      * The duration to run the application for which by default is in milliseconds.      * A value<= 0 will run forever.       * Adding a s indicates seconds - eg "5s" means 5 seconds.      *      * @parameter property="camel.duration"      *            default-value="-1"      */
DECL|field|duration
specifier|protected
name|String
name|duration
decl_stmt|;
comment|/**      * The classpath based application context uri that spring wants to get.      *      * @parameter property="camel.applicationContextUri"      */
DECL|field|applicationContextUri
specifier|protected
name|String
name|applicationContextUri
decl_stmt|;
comment|/**      * The filesystem based application context uri that spring wants to get.      *      * @parameter property="camel.fileApplicationContextUri"      */
DECL|field|fileApplicationContextUri
specifier|protected
name|String
name|fileApplicationContextUri
decl_stmt|;
comment|/**      * Project classpath.      *      * @parameter property="project.testClasspathElements"      * @required      * @readonly      */
DECL|field|classpathElements
specifier|private
name|List
argument_list|<
name|?
argument_list|>
name|classpathElements
decl_stmt|;
comment|/**      * The main class to execute.      *      * @parameter property="camel.mainClass"      *            default-value="org.apache.camel.spring.Main"      * @required      */
DECL|field|mainClass
specifier|private
name|String
name|mainClass
decl_stmt|;
comment|/**      * This method will run the mojo      */
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
try|try
block|{
name|executeWithoutWrapping
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|executeWithoutWrapping ()
specifier|public
name|void
name|executeWithoutWrapping
parameter_list|()
throws|throws
name|MalformedURLException
throws|,
name|ClassNotFoundException
throws|,
name|NoSuchMethodException
throws|,
name|IllegalAccessException
throws|,
name|MojoExecutionException
block|{
name|ClassLoader
name|oldClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|ClassLoader
name|newLoader
init|=
name|createClassLoader
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|newLoader
argument_list|)
expr_stmt|;
name|runCamel
argument_list|(
name|newLoader
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|oldClassLoader
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getClasspathElements ()
specifier|public
name|List
argument_list|<
name|?
argument_list|>
name|getClasspathElements
parameter_list|()
block|{
return|return
name|classpathElements
return|;
block|}
DECL|method|setClasspathElements (List<?> classpathElements)
specifier|public
name|void
name|setClasspathElements
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|classpathElements
parameter_list|)
block|{
name|this
operator|.
name|classpathElements
operator|=
name|classpathElements
expr_stmt|;
block|}
DECL|method|getDuration ()
specifier|public
name|String
name|getDuration
parameter_list|()
block|{
return|return
name|duration
return|;
block|}
DECL|method|setDuration (String duration)
specifier|public
name|void
name|setDuration
parameter_list|(
name|String
name|duration
parameter_list|)
block|{
name|this
operator|.
name|duration
operator|=
name|duration
expr_stmt|;
block|}
DECL|method|getApplicationContextUri ()
specifier|public
name|String
name|getApplicationContextUri
parameter_list|()
block|{
return|return
name|applicationContextUri
return|;
block|}
DECL|method|setApplicationContextUri (String applicationContextUri)
specifier|public
name|void
name|setApplicationContextUri
parameter_list|(
name|String
name|applicationContextUri
parameter_list|)
block|{
name|this
operator|.
name|applicationContextUri
operator|=
name|applicationContextUri
expr_stmt|;
block|}
DECL|method|getFileApplicationContextUri ()
specifier|public
name|String
name|getFileApplicationContextUri
parameter_list|()
block|{
return|return
name|fileApplicationContextUri
return|;
block|}
DECL|method|setFileApplicationContextUri (String fileApplicationContextUri)
specifier|public
name|void
name|setFileApplicationContextUri
parameter_list|(
name|String
name|fileApplicationContextUri
parameter_list|)
block|{
name|this
operator|.
name|fileApplicationContextUri
operator|=
name|fileApplicationContextUri
expr_stmt|;
block|}
DECL|method|getMainClass ()
specifier|public
name|String
name|getMainClass
parameter_list|()
block|{
return|return
name|mainClass
return|;
block|}
DECL|method|setMainClass (String mainClass)
specifier|public
name|void
name|setMainClass
parameter_list|(
name|String
name|mainClass
parameter_list|)
block|{
name|this
operator|.
name|mainClass
operator|=
name|mainClass
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|runCamel (ClassLoader newLoader)
specifier|protected
name|void
name|runCamel
parameter_list|(
name|ClassLoader
name|newLoader
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|NoSuchMethodException
throws|,
name|IllegalAccessException
throws|,
name|MojoExecutionException
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Running Camel in: "
operator|+
name|newLoader
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|newLoader
operator|.
name|loadClass
argument_list|(
name|mainClass
argument_list|)
decl_stmt|;
name|Method
name|method
init|=
name|type
operator|.
name|getMethod
argument_list|(
literal|"main"
argument_list|,
name|String
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|String
index|[]
name|arguments
init|=
name|createArguments
argument_list|()
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Starting the Camel Main with arguments: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|arguments
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
operator|new
name|Object
index|[]
block|{
name|arguments
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
name|Throwable
name|t
init|=
name|e
operator|.
name|getTargetException
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed: "
operator|+
name|t
argument_list|,
name|t
argument_list|)
throw|;
block|}
block|}
DECL|method|createArguments ()
specifier|protected
name|String
index|[]
name|createArguments
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|5
argument_list|)
decl_stmt|;
if|if
condition|(
name|applicationContextUri
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-applicationContext"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|applicationContextUri
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fileApplicationContextUri
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|add
argument_list|(
literal|"-fileApplicationContext"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|fileApplicationContextUri
argument_list|)
expr_stmt|;
block|}
name|args
operator|.
name|add
argument_list|(
literal|"-duration"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|getDuration
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|args
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
return|;
block|}
DECL|method|createClassLoader (ClassLoader parent)
specifier|public
name|ClassLoader
name|createClassLoader
parameter_list|(
name|ClassLoader
name|parent
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Using classpath: "
operator|+
name|classpathElements
argument_list|)
expr_stmt|;
name|int
name|size
init|=
name|classpathElements
operator|.
name|size
argument_list|()
decl_stmt|;
name|URL
index|[]
name|urls
init|=
operator|new
name|URL
index|[
name|size
index|]
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
name|String
name|name
init|=
operator|(
name|String
operator|)
name|classpathElements
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|urls
index|[
name|i
index|]
operator|=
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"URL: "
operator|+
name|urls
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|URLClassLoader
name|loader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|urls
argument_list|,
name|parent
argument_list|)
decl_stmt|;
return|return
name|loader
return|;
block|}
block|}
end_class

end_unit

