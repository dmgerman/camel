begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Documented
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Inherited
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * Annotation to be used to customise the deployment configured by the {@code CamelCdiRunner}.  *  * @see CamelCdiRunner  */
end_comment

begin_annotation_defn
annotation|@
name|Documented
annotation|@
name|Inherited
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
name|ElementType
operator|.
name|TYPE
argument_list|)
DECL|annotation|Beans
specifier|public
annotation_defn|@interface
name|Beans
block|{
comment|/**      * Returns the list of<a href="http://docs.jboss.org/cdi/spec/1.2/cdi-spec.html#alternatives">alternatives</a>      * to be selected in the application.      *<p/>      * Note that the declared alternatives are globally selected for the entire      * application. For example, if you have the following named bean in your      * application:      *<pre><code>      * {@literal @}Named("foo")      * public class FooBean {      *      * }      *</code></pre>      *      * It can be replaced in your test by declaring the following alternative      * bean:      *<pre><code>      * {@literal @}Alternative      * {@literal @}Named("foo")      * public class AlternativeBean {      *      * }      *</code></pre>      *      * And adding the {@code @Beans} annotation to you test class to activate it:      *<pre><code>      * {@literal @}RunWith(CamelCdiRunner.class)      * {@literal @}Beans(alternatives = AlternativeBean.class)      * public class TestWithAlternative {      *      * }      *</code></pre>      *      * @see javax.enterprise.inject.Alternative      */
DECL|method|alternatives ()
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|alternatives
argument_list|()
expr|default
block|{}
expr_stmt|;
comment|/**      * Returns the list of classes to be added as beans in the application.      *      * That can be used to add classes to the deployment for test purpose      * in addition to the test class which is automatically added as bean.      *      */
DECL|method|classes ()
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|classes
argument_list|()
expr|default
block|{}
expr_stmt|;
comment|/**      * Returns the list of classes whose packages are to be added for beans      * discovery.      *      * That can be used to add packages to the deployment for test purpose      * in addition to the test class which is automatically added as bean.      */
DECL|method|packages ()
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|packages
argument_list|()
expr|default
block|{}
expr_stmt|;
block|}
end_annotation_defn

end_unit

