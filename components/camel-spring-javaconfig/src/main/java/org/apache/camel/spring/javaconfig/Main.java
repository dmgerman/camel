begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.javaconfig
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|javaconfig
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
name|LinkedList
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|config
operator|.
name|java
operator|.
name|context
operator|.
name|JavaConfigApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_comment
comment|/**  * The Main class which takes the spring java config parameter  */
end_comment

begin_class
DECL|class|Main
specifier|public
class|class
name|Main
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|Main
block|{
DECL|field|basedPackages
specifier|private
name|String
name|basedPackages
decl_stmt|;
DECL|field|configClassesString
specifier|private
name|String
name|configClassesString
decl_stmt|;
DECL|method|Main ()
specifier|public
name|Main
parameter_list|()
block|{
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"bp"
argument_list|,
literal|"basedPackages"
argument_list|,
literal|"Sets the based packages of spring java config ApplicationContext"
argument_list|,
literal|"basedPackages"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setBasedPackages
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"cc"
argument_list|,
literal|"configClasses"
argument_list|,
literal|"Sets the config Class of spring java config ApplicationContext"
argument_list|,
literal|"configureClasses"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setConfigClassesString
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|main (String... args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|instance
operator|=
name|main
expr_stmt|;
name|main
operator|.
name|enableHangupSupport
argument_list|()
expr_stmt|;
name|main
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
DECL|method|setBasedPackages (String config)
specifier|public
name|void
name|setBasedPackages
parameter_list|(
name|String
name|config
parameter_list|)
block|{
name|basedPackages
operator|=
name|config
expr_stmt|;
block|}
DECL|method|getBasedPackages ()
specifier|public
name|String
name|getBasedPackages
parameter_list|()
block|{
return|return
name|basedPackages
return|;
block|}
DECL|method|setConfigClassesString (String config)
specifier|public
name|void
name|setConfigClassesString
parameter_list|(
name|String
name|config
parameter_list|)
block|{
name|configClassesString
operator|=
name|config
expr_stmt|;
block|}
DECL|method|getConfigClassesString ()
specifier|public
name|String
name|getConfigClassesString
parameter_list|()
block|{
return|return
name|configClassesString
return|;
block|}
DECL|method|getConfigClasses (String configureClasses)
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getConfigClasses
parameter_list|(
name|String
name|configureClasses
parameter_list|)
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|classes
init|=
name|configureClasses
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|className
range|:
name|classes
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|configClass
init|=
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|configClass
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|configClass
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
operator|.
name|toArray
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
name|answer
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
DECL|method|createDefaultApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createDefaultApplicationContext
parameter_list|()
block|{
name|ApplicationContext
name|parentContext
init|=
name|getParentApplicationContext
argument_list|()
decl_stmt|;
name|JavaConfigApplicationContext
name|jcApplicationContext
init|=
operator|new
name|JavaConfigApplicationContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentContext
operator|!=
literal|null
condition|)
block|{
name|jcApplicationContext
operator|.
name|setParent
argument_list|(
name|parentContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfigClassesString
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|configClasses
init|=
name|getConfigClasses
argument_list|(
name|getConfigClassesString
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
range|:
name|configClasses
control|)
block|{
name|jcApplicationContext
operator|.
name|addConfigClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|getBasedPackages
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|basePackages
init|=
name|getBasedPackages
argument_list|()
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|basePackage
range|:
name|basePackages
control|)
block|{
name|jcApplicationContext
operator|.
name|addBasePackage
argument_list|(
name|basePackage
argument_list|)
expr_stmt|;
block|}
block|}
name|jcApplicationContext
operator|.
name|refresh
argument_list|()
expr_stmt|;
return|return
name|jcApplicationContext
return|;
block|}
block|}
end_class

end_unit

