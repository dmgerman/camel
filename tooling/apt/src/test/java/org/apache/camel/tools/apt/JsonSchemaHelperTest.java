begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
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
name|tools
operator|.
name|apt
operator|.
name|helper
operator|.
name|JsonSchemaHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JsonSchemaHelperTest
specifier|public
class|class
name|JsonSchemaHelperTest
block|{
DECL|field|JAVADOC
specifier|private
specifier|static
specifier|final
name|String
name|JAVADOC
init|=
literal|""
operator|+
literal|"     * When in streaming mode, then the splitter splits the original message on-demand, and each splitted\n"
operator|+
literal|"     * message is processed one by one. This reduces memory usage as the splitter do not split all the messages first,\n"
operator|+
literal|"     * but then we do not know the total size, and therefore the {@link org.apache.camel.Exchange#SPLIT_SIZE} is empty.\n"
operator|+
literal|"     *<p/>\n"
operator|+
literal|"     * In non-streaming mode (default) the splitter will split each message first, to know the total size, and then\n"
operator|+
literal|"     * process each message one by one. This requires to keep all the splitted messages in memory and therefore requires\n"
operator|+
literal|"     * more memory. The total size is provided in the {@link org.apache.camel.Exchange#SPLIT_SIZE} header.\n"
operator|+
literal|"     *<p/>\n"
operator|+
literal|"     * The streaming mode also affects the aggregation behavior.\n"
operator|+
literal|"     * If enabled then Camel will process replies out-of-order, eg in the order they come back.\n"
operator|+
literal|"     * If disabled, Camel will process replies in the same order as the messages was splitted.\n"
operator|+
literal|"     *\n"
operator|+
literal|"     * @return the builder\n"
operator|+
literal|"     */\n"
decl_stmt|;
DECL|field|EXPECTED_OUT
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED_OUT
init|=
literal|"When in streaming mode, then the splitter splits the original message on-demand, and each splitted message is processed one by one."
operator|+
literal|" This reduces memory usage as the splitter do not split all the messages first, but then we do not know the total size, and therefore"
operator|+
literal|" the org.apache.camel.Exchange#SPLIT_SIZE is empty. In non-streaming mode (default) the splitter will split each message first, to know"
operator|+
literal|" the total size, and then process each message one by one. This requires to keep all the splitted messages in memory and therefore requires"
operator|+
literal|" more memory. The total size is provided in the org.apache.camel.Exchange#SPLIT_SIZE header. The streaming mode also affects the aggregation"
operator|+
literal|" behavior. If enabled then Camel will process replies out-of-order, eg in the order they come back. If disabled, Camel will process replies"
operator|+
literal|" in the same order as the messages was splitted."
decl_stmt|;
DECL|field|JAVADOC2
specifier|private
specifier|static
specifier|final
name|String
name|JAVADOC2
init|=
literal|""
operator|+
literal|"     **\n"
operator|+
literal|"     * Sets a grace period after which the mock endpoint will re-assert\n"
operator|+
literal|"     * to ensure the preliminary assertion is still valid.\n"
operator|+
literal|"     *<p/>\n"
operator|+
literal|"     * This is used for example to assert that<b>exactly</b> a number of messages \n"
operator|+
literal|"     * arrives. For example if {@link #expectedMessageCount(int)} was set to 5, then\n"
operator|+
literal|"     * the assertion is satisfied when 5 or more message arrives. To ensure that\n"
operator|+
literal|"     * exactly 5 messages arrives, then you would need to wait a little period\n"
operator|+
literal|"     * to ensure no further message arrives. This is what you can use this\n"
operator|+
literal|"     * {@link #setAssertPeriod(long)} method for.\n"
operator|+
literal|"     *<p/>\n"
operator|+
literal|"     * By default this period is disabled.\n"
operator|+
literal|"     *\n"
operator|+
literal|"     * @param period grace period in millis\n"
operator|+
literal|"     *\n"
decl_stmt|;
DECL|field|EXPECTED_OUT2
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED_OUT2
init|=
literal|"Sets a grace period after which the mock endpoint will re-assert to ensure the preliminary assertion "
operator|+
literal|"is still valid. This is used for example to assert that exactly a number of messages arrives. For example if expectedMessageCount(int)"
operator|+
literal|" was set to 5, then the assertion is satisfied when 5 or more message arrives. To ensure that exactly 5 messages arrives, then you would need"
operator|+
literal|" to wait a little period to ensure no further message arrives. This is what you can use this setAssertPeriod(long) method for. By default"
operator|+
literal|" this period is disabled."
decl_stmt|;
annotation|@
name|Test
DECL|method|testSanitizeJavaDoc ()
specifier|public
name|void
name|testSanitizeJavaDoc
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|s
init|=
literal|"* more memory. The total size is provided in the {@link org.apache.camel.Exchange#SPLIT_SIZE} header."
decl_stmt|;
name|String
name|s2
init|=
name|JsonSchemaHelper
operator|.
name|sanitizeDescription
argument_list|(
name|s
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"more memory. The total size is provided in the org.apache.camel.Exchange#SPLIT_SIZE header."
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|JsonSchemaHelper
operator|.
name|sanitizeDescription
argument_list|(
name|JAVADOC
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|EXPECTED_OUT
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|String
name|out2
init|=
name|JsonSchemaHelper
operator|.
name|sanitizeDescription
argument_list|(
name|JAVADOC2
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|EXPECTED_OUT2
argument_list|,
name|out2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

