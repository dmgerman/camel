begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.smpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * Created by engin on 30/11/2016.  */
end_comment

begin_class
DECL|class|SmppNLSTSplitterTest
specifier|public
class|class
name|SmppNLSTSplitterTest
block|{
annotation|@
name|Test
DECL|method|splitTurkishShortMessageWith155Character ()
specifier|public
name|void
name|splitTurkishShortMessageWith155Character
parameter_list|()
block|{
name|String
name|message
init|=
literal|"12345678901234567890123456789012345678901234567890123456789012345678901234567890"
operator|+
literal|"123456789012345678901234567890123456789012345678901234567890123456789012345"
decl_stmt|;
comment|// 155 single message
name|byte
name|turkishLanguageIdentifier
init|=
literal|0x01
decl_stmt|;
name|SmppSplitter
name|splitter
init|=
operator|new
name|SmppNLSTSplitter
argument_list|(
name|message
operator|.
name|length
argument_list|()
argument_list|,
name|turkishLanguageIdentifier
argument_list|)
decl_stmt|;
name|SmppSplitter
operator|.
name|resetCurrentReferenceNumber
argument_list|()
expr_stmt|;
name|byte
index|[]
index|[]
name|result
init|=
name|splitter
operator|.
name|split
argument_list|(
name|message
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_SINGLE_MSG_HEADER_LENGTH
block|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_IDENTIFIER
block|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_HEADER_LENGTH
block|,
name|turkishLanguageIdentifier
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|splitShortMessageWith156Character ()
specifier|public
name|void
name|splitShortMessageWith156Character
parameter_list|()
block|{
name|String
name|message
init|=
literal|"12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
operator|+
comment|// first part 149
literal|"0123456"
decl_stmt|;
comment|// second part 7
name|byte
name|turkishLanguageIdentifier
init|=
literal|0x01
decl_stmt|;
name|SmppSplitter
name|splitter
init|=
operator|new
name|SmppNLSTSplitter
argument_list|(
name|message
operator|.
name|length
argument_list|()
argument_list|,
name|turkishLanguageIdentifier
argument_list|)
decl_stmt|;
name|SmppSplitter
operator|.
name|resetCurrentReferenceNumber
argument_list|()
expr_stmt|;
name|byte
index|[]
index|[]
name|result
init|=
name|splitter
operator|.
name|split
argument_list|(
name|message
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_MULTI_MSG_HEADER_LENGTH
block|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_IDENTIFIER_SAR
block|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_SAR_LENGTH
block|,
literal|1
block|,
literal|2
block|,
literal|1
block|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_IDENTIFIER
block|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_HEADER_LENGTH
block|,
name|turkishLanguageIdentifier
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|}
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_MULTI_MSG_HEADER_LENGTH
block|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_IDENTIFIER_SAR
block|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_SAR_LENGTH
block|,
literal|1
block|,
literal|2
block|,
literal|2
block|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_IDENTIFIER
block|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_HEADER_LENGTH
block|,
name|turkishLanguageIdentifier
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|}
argument_list|,
name|result
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|String
name|firstShortMessage
init|=
operator|new
name|String
argument_list|(
name|result
index|[
literal|0
index|]
argument_list|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_MULTI_MSG_HEADER_REAL_LENGTH
argument_list|,
name|result
index|[
literal|0
index|]
operator|.
name|length
operator|-
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_MULTI_MSG_HEADER_REAL_LENGTH
argument_list|)
decl_stmt|;
name|String
name|secondShortMessage
init|=
operator|new
name|String
argument_list|(
name|result
index|[
literal|1
index|]
argument_list|,
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_MULTI_MSG_HEADER_REAL_LENGTH
argument_list|,
name|result
index|[
literal|1
index|]
operator|.
name|length
operator|-
name|SmppNLSTSplitter
operator|.
name|UDHIE_NLI_MULTI_MSG_HEADER_REAL_LENGTH
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|message
argument_list|,
name|firstShortMessage
operator|+
name|secondShortMessage
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

